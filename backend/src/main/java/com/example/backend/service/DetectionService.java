package com.example.backend.service;

import com.example.backend.entity.Comment;
import com.example.backend.repository.CommentRepository;
import com.example.backend.utils.TextPreprocessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.buffer.ByteDataBuffer;
import org.tensorflow.types.TString;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DetectionService {

    private static final Logger logger = LoggerFactory.getLogger(DetectionService.class);

    private SavedModelBundle model;

    @Autowired
    private CommentRepository commentRepository; // 注入CommentRepository

    /**
     * 初始化方法：加载TensorFlow模型
     * 注意：模型路径需与导出位置一致
     */
    @PostConstruct
    public void init() {
        try {
            // 模型路径：src/main/resources/model/1（需与Python导出路径一致）
            model = SavedModelBundle.load(
                    "src/main/java/com/example/backend/model/comment_classifier/1",
                    "serve"  // 标签名称（与导出时一致）
            );
            logger.info("✅ TensorFlow模型加载成功");
        } catch (Exception e) {
            logger.error("❌ 模型加载失败：{}", e.getMessage());
            throw new RuntimeException("模型初始化失败", e);
        }
    }

    /**
     * 评论检测核心方法
     * @param rawText 用户输入的原始文本
     * @return 包含检测结果的Map对象
     */
    public Map<String, Object> detectComment(String rawText) {
        // 0. 输入校验
        if (rawText == null || rawText.trim().isEmpty()) {
            logger.warn("收到空文本输入");
            return buildErrorResponse("输入文本不能为空");
        }

        try {
            // 1. 执行与Python一致的预处理
            String processedText = TextPreprocessor.process(rawText);
            logger.debug("预处理后文本：{}", processedText);

            // 2. 检查预处理结果是否有效
            if (processedText.isEmpty()) {
                logger.warn("预处理后文本为空：原始输入 - {}", rawText);
                return buildErrorResponse("无法处理该文本内容");
            }

            // 3. 执行模型推理
            Map<String, Object> response = runModelInference(processedText, rawText);

            // 4. 保存到数据库
            Comment comment = new Comment();
            comment.setRawText(rawText);
            comment.setProcessedText(processedText);
            comment.setStatus((String) response.get("status"));
            comment.setCreateTime(new Date());
            commentRepository.save(comment);

            return response;
        } catch (Exception e) {
            logger.error("检测过程中发生异常：{}", e.getMessage());
            return buildErrorResponse("服务器内部错误");
        }
    }

    /**
     * 执行TensorFlow模型推理
     */
    private Map<String, Object> runModelInference(String processedText, String rawText) {
        try (TString inputTensor = TString.scalarOf(processedText)) {
            // 4. 构建模型输入（注意输入节点名称需与导出模型一致）
            Tensor outputTensor = model.session()
                    .runner()
                    .feed("serving_default_text_input", inputTensor)  // 输入节点名称
                    .fetch("StatefulPartitionedCall")                 // 输出节点名称
                    .run()
                    .get(0);

            // 获取 ByteDataBuffer
            ByteDataBuffer byteDataBuffer = outputTensor.asRawTensor().data();

            // 通过 read() 方法读取所有数据到字节数组中
            byte[] byteArray = new byte[(int) byteDataBuffer.size()];  // 获取数据的大小
            byteDataBuffer.read(byteArray);  // 读取数据到字节数组

            // 将字节数组转换为 ByteBuffer
            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

            // 将 ByteBuffer 转换为 FloatBuffer
            FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
            float[] probs = new float[floatBuffer.remaining()];
            floatBuffer.get(probs);

            logger.debug("模型原始输出：{}", Arrays.toString(probs));

            // 构造响应
            return buildSuccessResponse(rawText, processedText, probs);
        }
    }





    /**
     * 构建成功响应
     */
    private Map<String, Object> buildSuccessResponse(String rawText, String processedText, float[] probs) {
        // 7. 获取预测类别（对应Python的LabelEncoder转换）
        // 原始标签映射：-1 → 0（负面），0 → 1（正常），1 → 2（正面）
        int predictedClass = argmax(probs);
        String status;
        switch (predictedClass) {
            case 0:  status = "负面（-1）"; break;
            case 1:  status = "正常（0）"; break;
            case 2:  status = "正面（1）"; break;
            default: status = "未知类别";
        }

        // 8. 构建返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("raw_text", rawText);          // 原始文本
        response.put("processed_text", processedText); // 预处理后文本
        response.put("status", status);            // 分类结果
        response.put("prob_negative", probs[0]);    // 负面概率
        response.put("prob_normal", probs[1]);      // 正常概率
        response.put("prob_positive", probs[2]);    // 正面概率
        return response;
    }

    /**
     * 构建错误响应
     */
    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", message);
        return response;
    }

    /**
     * 辅助方法：获取概率最大值的索引
     */
    private int argmax(float[] probs) {
        int maxIndex = 0;
        for (int i = 1; i < probs.length; i++) {
            if (probs[i] > probs[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * 安全关闭方法（可选）
     */
    public void close() {
        if (model != null) {
            model.close();
            logger.info("🗑️ TensorFlow模型已安全释放");
        }
    }
}

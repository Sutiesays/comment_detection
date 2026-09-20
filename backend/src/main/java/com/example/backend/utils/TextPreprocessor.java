package com.example.backend.utils;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.lang.String;

public class TextPreprocessor {
    private static final Pattern URL_PATTERN = Pattern.compile("http\\S+|@\\S+|#\\S+");
    private static final Pattern NON_CHINESE_PATTERN = Pattern.compile("[^\\u4e00-\\u9fa5\\s]");
    private static final JiebaSegmenter SEGMENTER = new JiebaSegmenter();
    private static Set<String> STOP_WORDS;

    static {
        // 加载停用词表（路径需与Python端一致）
        try {
            STOP_WORDS = Files.lines(Paths.get("C:/Users/28432/Desktop/commentAnalysis/model/data/external/chinese_stopwords.txt"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException("无法加载停用词表", e);
        }

    }

    /**
     * 完整文本预处理流程（与Python完全一致）
     * @param rawText 原始输入文本
     * @return 预处理后的文本（空格分隔的分词结果）
     */
    public static String process(String rawText) {
        // 1. 清洗文本
        String cleaned = cleanText(rawText);
        // 2. 中文分词
        List<String> words = segment(cleaned);
        // 3. 停用词过滤
        return filterStopWords(words);
    }

    /**
     * 文本清洗（去除URL、非中文字符）
     */
    private static String cleanText(String text) {
        // 去除URL、@、#标签
        String step1 = URL_PATTERN.matcher(text).replaceAll("");
        // 只保留中文和空格
        String step2 = NON_CHINESE_PATTERN.matcher(step1).replaceAll("");
        return step2.trim();
    }

    /**
     * 中文分词（使用Jieba与Python相同模式）
     */
    private static List<String> segment(String text) {
        return SEGMENTER.process(text, JiebaSegmenter.SegMode.SEARCH).stream()
                .map(token -> token.word)  // 直接访问公共字段（适用于1.0.2+）
                .collect(Collectors.toList());
    }

    /**
     * 停用词过滤并拼接为字符串
     */
    private static String filterStopWords(List<String> words) {
        return words.stream()
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.joining(" "));
    }
}

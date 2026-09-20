import tensorflow as tf

# 加载已训练好的模型（示例代码需要替换实际模型加载方式）
model = tf.keras.models.load_model('../saved_models/comment_model')

# 导出为SavedModel格式
export_path = '../../backend/src/main/java/com/example/backend/model/comment_classifier/1'
tf.saved_model.save(model, export_path)

print(f"Model exported to {export_path}")
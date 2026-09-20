import tensorflow as tf
from keras.layers import Embedding, LSTM, Dense, Bidirectional, Dropout
from keras.models import Sequential
from keras.regularizers import l2
from keras.optimizers import Adam
from keras.callbacks import EarlyStopping
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import LabelEncoder
import os

os.environ["CUDA_VISIBLE_DEVICES"] = "-1"  # 强制使用 CPU

# 读取训练数据
train_df = pd.read_csv("../data/processed/train.csv")
val_df = pd.read_csv("../data/processed/val.csv")

# 标签编码（转换为 0, 1, 2）
train_labels = train_df["label"] + 1
val_labels = val_df["label"] + 1

# 文本向量化（调整参数）
vectorizer = tf.keras.layers.TextVectorization(
    max_tokens=20000,
    output_sequence_length=150
)
vectorizer.adapt(train_df["text"].tolist())

# 转换为数值序列
X_train_vectorized = vectorizer(np.array(train_df["text"])).numpy()
X_val_vectorized = vectorizer(np.array(val_df["text"])).numpy()

# 构建优化后的模型
model = Sequential([
    Embedding(input_dim=20000, output_dim=256),
    Bidirectional(LSTM(128, return_sequences=True, kernel_regularizer=l2(0.01))),
    Dropout(0.3),
    Bidirectional(LSTM(64)),
    Dense(64, activation="relu", kernel_regularizer=l2(0.01)),
    Dense(3, activation="softmax")
])

# 编译模型（调整优化器）
model.compile(
    loss="sparse_categorical_crossentropy",
    optimizer=Adam(learning_rate=1e-4),
    metrics=["accuracy"]
)

# 训练模型（添加早停法）
history = model.fit(
    X_train_vectorized,
    train_labels,
    validation_data=(X_val_vectorized, val_labels),
    epochs=10,
    batch_size=64,
    callbacks=[EarlyStopping(patience=3, restore_best_weights=True)]
)

# 保存模型
model.save("../saved_models/comment_model")
print("模型训练完成并已保存！")

# 打印训练过程中的损失和准确率
print("训练过程中的损失和准确率：")
print("训练集损失：", history.history['loss'])
print("验证集损失：", history.history['val_loss'])
print("训练集准确率：", history.history['accuracy'])
print("验证集准确率：", history.history['val_accuracy'])




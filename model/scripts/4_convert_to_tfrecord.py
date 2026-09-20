import pandas as pd
import tensorflow as tf
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# 读取训练集
train_df = pd.read_csv("../data/processed/train.csv")

# 定义序列化函数
def serialize_example(text, label):
    feature = {
        "text": tf.train.Feature(bytes_list=tf.train.BytesList(value=[text.encode("utf-8")])),
        "label": tf.train.Feature(int64_list=tf.train.Int64List(value=[label]))
    }
    return tf.train.Example(features=tf.train.Features(feature=feature))

# 保存为TFRecord
with tf.io.TFRecordWriter("../data/external/train.tfrecord") as writer:
    for _, row in train_df.iterrows():
        example = serialize_example(row["text"], row["label"])
        writer.write(example.SerializeToString())
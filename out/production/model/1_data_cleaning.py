import pandas as pd
import re
import jieba


df = pd.read_csv("../data/raw/comments.csv", encoding="GBK")

# 清洗文本（去除特殊符号、数字、英文字符等）
def clean_text(text):
    text = re.sub(r"http\S+|@\S+|#\S+", "", text)  # 去除URL、@用户、#话题
    text = re.sub(r"[^\u4e00-\u9fa5\s]", "", text)  # 只保留中文字符和空格，去除英文、数字和标点符号
    return text.strip()

df["text"] = df["text"].apply(clean_text)

# 中文分词与停用词过滤
with open("../data/external/chinese_stopwords.txt", "r", encoding="utf-8") as f:
    stopwords = set(f.read().splitlines())

def tokenize(text):
    words = jieba.cut(text)
    return " ".join([w for w in words if w not in stopwords])

df["text"] = df["text"].apply(tokenize)

# 保存预处理后的数据
df.to_csv("../data/processed/cleaned_comments.csv", index=False)

# 在 1_data_cleaning.py 中添加：
print("原始数据行数:", len(df))  # 检查是否读取到数据
print("清洗后数据行数:", len(df[df["text"] != ""]))  # 检查清洗后是否有有效文本
df.to_csv("../data/processed/cleaned_comments.csv", index=False)
print("清洗后数据已保存！")  # 检查是否执行到保存步骤

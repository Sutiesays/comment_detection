import pandas as pd
from imblearn.over_sampling import SMOTE
from sklearn.preprocessing import LabelEncoder
from sklearn.feature_extraction.text import TfidfVectorizer

# 读取清洗后的数据
df = pd.read_csv("../data/processed/cleaned_comments.csv")

# ------ 检查并处理潜在空值 ------
# 删除包含空值的行
df = df.dropna(subset=["text", "label"])

# 再次过滤空白文本（确保无残留）
df = df[df["text"].str.strip().astype(bool)]

# ------ 标签编码 ------
le = LabelEncoder()
df["label"] = le.fit_transform(df["label"])

# ------ 提取特征和标签 ------
X = df["text"]
y = df["label"]

# ------ 文本向量化 ------
vectorizer = TfidfVectorizer()
X_vectorized = vectorizer.fit_transform(X)

# ------ SMOTE过采样 ------
smote = SMOTE(random_state=42)
X_resampled, y_resampled = smote.fit_resample(X_vectorized, y)

# ------ 合并平衡后的数据 ------
# 将稀疏矩阵转换为文本列表（去除空字符串）
texts = vectorizer.inverse_transform(X_resampled)
texts = [" ".join(t) if len(t) > 0 else "未知" for t in texts]  # 处理空列表

balanced_df = pd.DataFrame({
    "text": texts,
    "label": le.inverse_transform(y_resampled)
})

# 保存平衡后的数据
balanced_df.to_csv("../data/processed/balanced_comments.csv", index=False)
print("数据平衡完成！平衡后数据行数:", len(balanced_df))

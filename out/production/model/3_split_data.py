import pandas as pd
from sklearn.model_selection import train_test_split

# 读取平衡后的数据（或直接使用清洗后的数据）
df = pd.read_csv("../data/processed/balanced_comments.csv")

# 划分数据集（70%训练，15%验证，15%测试）
train_df, temp_df = train_test_split(df, test_size=0.3, random_state=42)
val_df, test_df = train_test_split(temp_df, test_size=0.5, random_state=42)

# 保存为CSV
train_df.to_csv("../data/processed/train.csv", index=False)
val_df.to_csv("../data/processed/val.csv", index=False)
test_df.to_csv("../data/processed/test.csv", index=False)
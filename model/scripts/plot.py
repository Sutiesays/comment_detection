import matplotlib.pyplot as plt
import matplotlib

# 使用非GUI后端，适用于服务器或没有图形界面的环境
matplotlib.use('Agg')

# 设置中文字体
plt.rcParams["font.family"] = ["SimHei", "WenQuanYi Micro Hei", "Heiti TC"]

# 训练数据（从你的日志中提取）
epochs = list(range(1, 11))

train_loss = [
    5.8082, 2.4077, 1.1719, 0.7299, 0.5516,
    0.4640, 0.4020, 0.3584, 0.3231, 0.2975
]
val_loss = [
    3.5708, 1.7067, 1.0993, 0.8919, 0.8423,
    0.8006, 0.7706, 0.7904, 0.7669, 0.7357
]

train_acc = [
    0.4817, 0.7479, 0.8676, 0.9147, 0.9368,
    0.9464, 0.9543, 0.9563, 0.9608, 0.9645
]
val_acc = [
    0.6279, 0.7577, 0.8055, 0.8250, 0.8358,
    0.8344, 0.8399, 0.8252, 0.8319, 0.8385
]

# 设置画布
plt.figure(figsize=(12, 5))

# ------------------------------
# 绘制 Loss 曲线
# ------------------------------
plt.subplot(1, 2, 1)
plt.plot(epochs, train_loss, 'b-o', label='训练集损失值')
plt.plot(epochs, val_loss, 'r--s', label='验证集损失值')
plt.title('训练集vs验证集损失值折线图')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.grid(linestyle='--', alpha=0.5)
plt.legend()

# ------------------------------
# 绘制 Accuracy 曲线
# ------------------------------
plt.subplot(1, 2, 2)
plt.plot(epochs, train_acc, 'g-o', label='训练集准确率')
plt.plot(epochs, val_acc, 'm--s', label='验证集准确率')
plt.title('训练集vs验证集准确率折线图')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.grid(linestyle='--', alpha=0.5)
plt.legend()

# 调整布局并保存
plt.tight_layout()
plt.savefig('training_metrics.png', dpi=300, bbox_inches='tight')

# 因为是Agg后端，这里不需要 plt.show()
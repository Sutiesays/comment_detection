from keras.utils.vis_utils import model_to_dot
from IPython.display import SVG
import tensorflow as tf
import os
import tensorflow as tf
tf.config.set_visible_devices([], 'GPU')

os.environ["CUDA_VISIBLE_DEVICES"] = "-1"  # 强制使用 CPU
model = tf.keras.models.load_model('../saved_models/comment_model')
SVG(model_to_dot(model, show_shapes=True).create(prog='dot', format='svg'))
<template>
  <div class="comment-form">
    <h3>提交评论</h3>
    <form @submit.prevent="handleSubmit">
      <textarea v-model="comment" placeholder="请输入评论内容"></textarea>
      <button type="submit">提交评论</button>
    </form>
    <div v-if="response">
      <p>检测结果: {{ response.status }}</p>
      <p>恶意概率: {{ response.prob_negative }}%</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      comment: '',
      response: null
    };
  },
  methods: {
    async handleSubmit() {
      try {
        const res = await axios.post('/api/comments', { text: this.comment });
        this.response = res.data;
      } catch (error) {
        console.error('提交评论时出错', error);
      }
    }
  }
};
</script>

<style scoped>
/* 样式 */
</style>

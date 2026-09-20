<template>
  <div class="admin-dashboard">
    <h2>管理员仪表板</h2>
    <div class="statistics">
      <h3>评论检测统计</h3>
      <p>准确率: {{ statistics.accuracy }}%</p>
      <p>误报率: {{ statistics.falsePositiveRate }}%</p>
    </div>
    <div class="comment-management">
      <h3>评论管理</h3>
      <ul>
        <li v-for="comment in comments" :key="comment.id">
          <p>{{ comment.text }}</p>
          <button>处理</button>
        </li>
      </ul>
    </div>
    <router-link to="/statistics">查看详细统计数据</router-link>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      statistics: {},
      comments: []
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const statsResponse = await axios.get('/api/statistics');
        this.statistics = statsResponse.data;

        const commentsResponse = await axios.get('/api/comments');
        this.comments = commentsResponse.data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    }
  }
};
</script>

<style scoped>
/* 样式 */
</style>

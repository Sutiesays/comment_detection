<template>
  <div class="statistics-page">
    <h2>评论检测统计</h2>
    <p>准确率: {{ stats.accuracy }}</p>
    <p>误报率: {{ stats.falsePositiveRate }}</p>
    <p>召回率: {{ stats.recallRate }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      stats: {}
    };
  },
  mounted() {
    this.fetchStats();
  },
  methods: {
    async fetchStats() {
      try {
        const res = await axios.get('/api/statistics');
        this.stats = res.data;
      } catch (err) {
        console.error('Error fetching statistics', err);
      }
    }
  }
};
</script>

<style scoped>
/* 样式 */
</style>

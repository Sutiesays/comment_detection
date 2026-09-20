<template>
  <div class="login-container">
    <form @submit.prevent="handleLogin">
      <h2>登录</h2>
      <input v-model="username" type="text" placeholder="用户名" />
      <input v-model="password" type="password" placeholder="密码" />
      <p v-if="error" class="error">{{ error }}</p>
      <button type="submit">登录</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: ''
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('/api/login', {
          username: this.username,
          password: this.password
        });

        if (response.data.role === 'admin') {
          this.$router.push({ name: 'admin-dashboard' });
        } else if (response.data.role === 'user') {
          this.$router.push({ name: 'user-dashboard' });
        } else {
          this.error = '登录失败，未知用户';
        }
      } catch (err) {
        this.error = '登录失败，请检查用户名和密码';
      }
    }
  }
};
</script>

<style scoped>
/* 样式 */
</style>

import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

import axios from 'axios';
axios.defaults.baseURL = 'http://localhost:8080/api'; // 假设后端API的根路径为/api

createApp(App).use(router).mount('#app');

import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import AdminDashboardView from '../views/AdminDashboardView.vue';
import UserDashboardView from '../views/UserDashboardView.vue';
import Statistics from '../components/Statistics.vue';

const routes = [
    { path: '/', component: LoginView },
    { path: '/admin-dashboard', name: 'admin-dashboard', component: AdminDashboardView },
    { path: '/user-dashboard', name: 'user-dashboard', component: UserDashboardView },
    { path: '/statistics', component: Statistics }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;

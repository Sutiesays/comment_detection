import axios from 'axios';

export default {
    login(username, password) {
        return axios.post('/login', { username, password });
    },
    getStatistics() {
        return axios.get('/statistics');
    },
    getComments() {
        return axios.get('/comments');
    },
    submitComment(comment) {
        return axios.post('/comments', { text: comment });
    }
};

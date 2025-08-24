import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.css';
import axios from 'axios'
import '@fortawesome/fontawesome-free/css/all.css';
import '@fortawesome/fontawesome-free/js/all.js';
import 'jolty-ui';


axios.defaults.baseURL
= 'http://localhost:8084/'


createApp(App).use(router).mount('#app')

import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import DetailsView from '@/views/DetailsView.vue'
import SrsView from '@/views/SrsView.vue'
import HistoryView from '@/views/HistoryView.vue'


const routes = [
  {
    path: '/',
    name: 'HomeView',
    component: HomeView
  },
  {
    path: '/details/:approach', 
    name: 'DetailsView',
    component: DetailsView,
    props: true
  },

  {
    path: '/srs/:approach',
    name: 'SrsView',
    component: SrsView,
    props: true
  },
  {
    path: '/history',
    name: 'HistoryView',
    component: HistoryView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

import { createRouter, createWebHistory } from 'vue-router'
import CyberCigarette from '../views/CyberCigarette.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: CyberCigarette
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 
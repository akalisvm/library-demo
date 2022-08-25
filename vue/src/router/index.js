import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'layout',
    component: Layout,
    redirect: "/login",
    children: [
      {
        path: 'home',
        name: 'home',
        component: () => import("@/views/Home")
      },
      {
        path: 'user',
        name: '用户列表',
        component: () => import("@/views/User")
      },
      {
        path: 'book',
        name: '图书列表',
        component: () => import("@/views/Book")
      },
      {
        path: 'borrow',
        name: '借阅管理',
        component: () => import("@/views/Borrow")
      },
      {
        path: 'comment',
        name: '管理员公告',
        component: () => import("@/views/Comment")
      },
      {
        path: '/person',
        name: '个人资料',
        component: () => import("@/views/Person")
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'register',
    component: () => import("@/views/Register")
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

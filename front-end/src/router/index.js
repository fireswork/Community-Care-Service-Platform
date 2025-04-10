import { createRouter, createWebHistory } from 'vue-router'
import {
  TeamOutlined,
  ProfileOutlined,
  ScheduleOutlined,
  BarChartOutlined,
  UserOutlined
} from '@ant-design/icons-vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layouts/BasicLayout.vue'),
    redirect: '/category',
    children: [
      {
        path: '/category',
        name: 'Category',
        component: () => import('@/views/category.vue'),
        meta: { title: '分类管理', icon: TeamOutlined }
      },
      {
        path: '/info',
        name: 'Info',
        component: () => import('@/views/info.vue'),
        meta: { title: '基础信息管理', icon: ProfileOutlined }
      },
      {
        path: '/record',
        name: 'Record',
        component: () => import('@/views/record.vue'),
        meta: { title: '关怀记录管理', icon: ScheduleOutlined }
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics.vue'),
        meta: { title: '统计分析', icon: BarChartOutlined }
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('@/views/user.vue'),
        meta: { title: '用户管理', icon: UserOutlined }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  next();
})

export default router 
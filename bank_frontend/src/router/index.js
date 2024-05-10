import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import { useLoginStore } from '@/stores/loginStore'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/accounts',
      name: 'accounts',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AcountsView.vue'),
      meta: { requiresAuth: true }    },
    {
      path: '/atm',
      name: 'atm',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: HomeView
    },
    {
      path: '/error',
      name: 'error',
      component: HomeView
    },
    {
      path: '/admin',
      name: 'admin',
      component: HomeView
    },
    {
      path: '/logout',
      name: 'logout',
      meta: { requiresAuth: true },
      beforeEnter: (to, from, next) => {
        const loginStore = useLoginStore();
        loginStore.logout();
        next('/');
      }
    },
    
  ]
});

router.beforeEach((to, from, next) => {
  const loginStore = useLoginStore();

  if(loginStore.isLoggedIn && (to.path === '/login' || to.path === '/register')){
      next('/');
  }

  if(to.meta.requiresAuth){
    
    if(!loginStore.isLoggedIn){
      next('/login');
    } else {
      if(to.meta.requiresAdmin && loginStore.requestUserData.usertype !== 'admin'){
        next('/');

      } else {
        next();
      }
    }
  } else {
    next();
  }
});

export default router

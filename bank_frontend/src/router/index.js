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
      component: () => import('../views/AcountsView.vue'),
      meta: { requiresAuth: true, requiresCustomer: true }
    },
    {
      path: '/atm',
      name: 'atm',
      component: () => import('../views/ATMview.vue'),
      meta: { requiresAuth: true, requiresCustomer: true }
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegistrationView.vue')
    },
    {
      path: '/error',
      name: 'error',
      component: HomeView
    },
    {
      path: '/admin',
      name: 'admin',
      component: HomeView,
      meta: { requiresAuth: true, requiresAdmin: true }
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
    {
      path: '/transfer',
      name: 'Transfer',
      component: () => import('../views/LoginView.vue'),
      meta: { requiresAuth: true }
    }
    
  ]
});

router.beforeEach((to, from, next) => {
  const loginStore = useLoginStore();

  if(loginStore.isLoggedIn && (to.path === '/login' || to.path === '/register')){
      next('/');
  }

  if(to.meta.requiresAuth){

    if(!loginStore.isLoggedIn){

        next({path: '/login',
        query: { redirect: to.fullPath }})

    } else {
      if(to.meta.requiresAdmin && !loginStore.hasUsertype('ADMIN')){
        next('/');

      } else if(to.meta.requiresCustomer && !loginStore.hasUsertype('CUSTOMER')) {
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

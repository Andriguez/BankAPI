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
      meta: { requiresAuth: true, requiresAdmin: true }
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
      component: () => import('../views/TransferView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/registrations',
      name: 'Registrations',
      component: () => import('../views/RegistrationsView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/users',
      name: 'Users',
      component: () => import('../views/UsersView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/details',
      name: 'Details',
      component: () => import('@/components/admin/users_details.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/transactions',
      name: 'Transactions',
      component: () => import('@/views/TransactionsView.vue'),
      meta: {requiresAuth: true, requiresAdmin: true}
    },
    {
      path: '/transactions/:type',
      name: 'TransactionsWithType',
      component: () => import('@/views/TransactionsView.vue'),
      meta: {requiresAuth: true, requiresCustomer: true}
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

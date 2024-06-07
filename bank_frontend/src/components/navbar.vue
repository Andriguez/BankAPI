<template>
  <header v-if="$route.path !== '/atm'" class="p-3 border-bottom">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <router-link to="/" class="d-flex align-items-center mb-1 mb-lg-0 link-body-emphasis text-decoration-none">
          <img src="./../assets/logo.png" alt="logo" width="80" height="55">
        </router-link>

        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li><router-link to="/" class="nav-link px-2 link-body-emphasis">Home</router-link></li>
          <li v-if="isLoggedIn() && hasUsertype('CUSTOMER')" class="dropdown text-end">
          <a href="#" class="d-block nav-link link-body-emphasis pt-2 text-decoration-none" data-bs-toggle="dropdown" aria-expanded="false">
            Accounts
          </a>
          <ul class="dropdown-menu dropdown-menu-dark text-small">
            <li><router-link class="dropdown-item" to="/transactions/current">Current</router-link></li>
            <li><router-link class="dropdown-item" to="/transactions/savings">Savings</router-link></li>
          </ul>
        </li>
          <li v-if="!isLoggedIn()"><router-link to="/login" class="nav-link px-2 link-body-emphasis">Login</router-link></li>
          <li v-if="isLoggedIn() && hasUsertype('ADMIN')" class="dropdown text-end">
          <a href="#" class="d-block nav-link link-body-emphasis pt-2 text-decoration-none" data-bs-toggle="dropdown" aria-expanded="false">
            Users
          </a>
          <ul class="dropdown-menu dropdown-menu-dark text-small">
            <li><router-link class="dropdown-item" to="/registrations">Registrations</router-link></li>
            <li><router-link class="dropdown-item" to="/users">Customers</router-link></li>
          </ul>
        </li>
          <li v-if="isLoggedIn() && hasUsertype('ADMIN')"><router-link to="/" class="nav-link px-2 link-body-emphasis">Transactions</router-link></li>
          <li v-if="isLoggedIn() && hasUsertype('ADMIN')"><router-link to="/accounts" class="nav-link px-2 link-body-emphasis">Accounts</router-link></li>
          <li v-if="isLoggedIn() && !hasUsertype('GUEST')"><router-link to="/transfer" class="nav-link px-2 link-body-emphasis">Transfer</router-link></li>

        </ul>
        
        <div v-if="isLoggedIn()" class="dropstart text-end">
          <a href="#" class="d-block link-body-emphasis text-decoration-none" data-bs-toggle="dropdown" aria-expanded="false">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="#942EE5" class="bi bi-person-circle" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
            <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
            </svg>
        </a>
          <ul class="dropdown-menu dropdown-menu-dark text-small">
            <li class="px-3"><strong>{{ loginStore.name }}</strong></li>
            <li><hr class="dropdown-divider"></li>
            <li><router-link v-if="!hasUsertype('ADMIN')" class="dropdown-item" to="/details">User Details</router-link></li>
            <li><router-link class="dropdown-item" to="/logout">Log out</router-link></li>
          </ul>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { useLoginStore } from '../stores/loginStore';

export default {
    name: 'Navigation',
    data() {
        return {
            loginStore: useLoginStore(),
        };
    },
    mounted(){
        if (!this.loginStore) {
            console.error('Failed to initialize login store');
            return;
        }
    },
    methods: {
        isLoggedIn(){
            if (!this.loginStore) {
                return false;
            }
            return this.loginStore.isLoggedIn;
        },
        hasUsertype(usertype){
          return this.loginStore.hasUsertype(usertype);
        }
    }
}

</script>

<style>
header{
  background-color: #9D9D9D;
  
  .nav{
    li {
      color: black !important;
      font-size: 20px;
      font-weight: bold;
    }

    router-link{
      color: black !important;
      
    }
  
    li:hover {
      color: #942EE5 !important;
      text-decoration: underline;
    }
  
  }

}
</style>
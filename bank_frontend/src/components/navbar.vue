<template>
  <header class="p-3 mb-3 border-bottom">
    <div class="container">
      <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
        <router-link to="/" class="d-flex align-items-center mb-1 mb-lg-0 link-body-emphasis text-decoration-none">
          <img src="./../assets/logo.png" alt="logo" width="80" height="55">
        </router-link>

        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li><router-link to="/" class="nav-link px-2 link-body-emphasis">Home</router-link></li>
          <li v-if="isLoggedIn()"><router-link to="/accounts" class="nav-link px-2 link-body-emphasis">Accounts</router-link></li>
          <li v-if="!isLoggedIn()"><router-link to="/login" class="nav-link px-2 link-body-emphasis">Login</router-link></li>
        </ul>
        
        <div v-if="isLoggedIn()" class="dropdown text-end">
          <a href="#" class="d-block link-body-emphasis text-decoration-none dropdown-toggle text-light" data-bs-toggle="dropdown" aria-expanded="false">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="white" class="bi bi-person-circle" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
            <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8m8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1"/>
            </svg>
        </a>
          <ul class="dropdown-menu text-small">
            <li><router-link class="dropdown-item" to="/">User Details</router-link></li>
            <li><hr class="dropdown-divider"></li>
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
        }
    }
}

</script>

<style>
header{
  background-color: #9D9D9D;
  
  li{
    color: black !important;
    font-size: 20px;
    font-weight: bold;
  }

}
</style>
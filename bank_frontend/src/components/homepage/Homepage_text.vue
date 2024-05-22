<script>
import { useLoginStore} from '@/stores/loginStore.js'

export default {
    data() {
        return {
            loginStore: useLoginStore(),
        }
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

<template>
    <div v-if="!isLoggedIn()" class="container p-5" style="text-align: center;">
    <h1 class="py-4">Welcome to <em>Holla Dolla Bank</em></h1>
    <h3 class="py-2" style="color: white;">Your Trusted Financial Partner</h3>
    <h5>At <em>Holla Dolla Bank</em>, we believe in empowering your financial journey with innovative solutions and personalized services. Whether you're looking to open a new account, secure a loan, or explore investment opportunities, our dedicated team is here to assist you every step of the way.</h5>
  </div>

  <div v-if="isLoggedIn() && hasUsertype('GUEST')" class="container p-5" style="text-align: center;">
    <h1 class="py-4">We're happy you joined us at <em>Holla Dolla Bank</em></h1>
    <h3 class="py-2" style="color: white;">Your Trusted Financial Partner</h3>
    <h5>As a newly registered customer, your account is currently under review. Our team will carefully assess your information, and once approved, you’ll gain full access to our services. Thank you again for choosing us, and we appreciate your patience!</h5>
  </div>

  <div v-if="isLoggedIn() && hasUsertype('CUSTOMER')" class="container p-5" style="text-align: center;">
    <h1 class="py-4">Welcome back to <em>Holla Dolla Bank</em></h1>
    <h3 class="py-2" style="color: white;">Your Trusted Financial Partner</h3>
    <h5>At <em>Holla Dolla Bank</em>, we believe in empowering your financial journey with innovative solutions and personalized services. Whether you're looking to open a new account, secure a loan, or explore investment opportunities, our dedicated team is here to assist you every step of the way.</h5>
  </div>

  <div v-if="isLoggedIn() && hasUsertype('ADMIN')" class="container p-5" style="text-align: center;">
    <h1 class="py-4"><em>Hello Again, Employee: {{ loginStore.name }}</em></h1>
    <h3 class="py-2" style="color: white;">You make this Bank a Trusted Financial Partner for our customers</h3>
    <h5>Thanks to you, <em>Holla Dolla Bank</em>, helps empowering our customers' financial journey with innovative solutions and personalized services. Whether you're opening a new account, secure a loan, or explore investment opportunities, we acknowledge your dedication to the team, assisting customers' every step of the way.</h5>
  </div>

  
</template>
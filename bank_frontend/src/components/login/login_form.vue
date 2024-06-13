<template>
        <div class="container d-flex justify-content-center">
    <form>
            <div class="form-floating py-2">
                <input type="email" class="form-control" id="floatingInput" placeholder="Email" v-model="email">
                <label for="floatingInput">Email</label>
            </div>
            <div class="form-floating py-2">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password"  v-model="password">
                <label for="floatingPassword">Password</label>
            </div>

            <div class="container d-flex py-4 justify-content-center flex-nowrap">
                <a class="nav-link" @click="login()" style="font-size: 27px; cursor: pointer;">login</a>
                <router-link class="nav-link" to="/register" style="font-size: 27px">sign up</router-link>
            </div>
        </form>
        </div>
</template>
<script>
import { useLoginStore } from '@/stores/loginStore'

export default {
    name: 'LoginForm',
    
    data(){
        return {
        email : '',
        password : ''
    };
    },
    methods: {
        login() {
            this.loginStore.requestLogin(this.email, this.password)
            .then(() => {
          const redirectPath = this.$route.query.redirect || '/';
          this.$router.replace(redirectPath);
        })

            .catch ((error)=>{
                this.errorMessage = error;
                alert(error.response.data);
            })
        },
    },
    mounted(){
        this.loginStore = useLoginStore();
    }
}
</script>

<style>
form{
    padding-top: 10%;
    
    input {
        border: 2px solid #942EE5 !important;
    }

    a{
        color: #942EE5 !important;
        border: 2px solid #942EE5 !important;
        border-radius: 20% !important;
        font-size: 16px !important;
        padding: 6% !important;
        margin: 5% !important;
    }

    a:hover{
        color: #9574b1 !important;
        border-color: #4e0789 !important;
        }
}
</style>
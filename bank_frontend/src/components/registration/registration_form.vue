<template>
    <main style="text-align: center;">
        <div class="container py-3">
                <h5 style="color: blueviolet;">Fill in your personal information to sign up!</h5>
            </div>
        <div class="container d-flex justify-content-center" style="width: 800px">
    <form>
        <div class="container d-flex flex-wrap">
            <div class="form-floating px-2 py-2">
                <input type="text" class="form-control" id="floatingFirstName" placeholder="First Name" v-model="firstname">
                <label for="floatingFirstName">First Name</label>
            </div>
            <div class="form-floating px-2 py-2">
                <input type="text" class="form-control" id="floatingLastName" placeholder="Last Name" v-model="lastname">
                <label for="floatingLastName">Last Name</label>
            </div></div>

            <div class="container d-flex flex-wrap">
            <div class="form-floating px-2 py-2">
                <input type="text" class="form-control" id="floatingBSNNumber" placeholder="BSN Number" v-model="bsn">
                <label for="floatingPhoneNumber">BSN Number</label>
            </div>
            <div class="form-floating px-2 py-2">
                <input type="text" class="form-control" id="floatingPhoneNumber" placeholder="Phone Number" v-model="phone">
                <label for="floatingPhoneNumber">Phone Number</label>
            </div> </div>

        <div class="container d-flex flex-wrap">
            <div class="form-floating px-2 py-2">
                <input type="email" class="form-control" id="floatingInput" placeholder="Email" v-model="email">
                <label for="floatingInput">Email</label>
            </div>
            <div class="form-floating px-2 py-2">
                <input type="password" class="form-control" id="floatingPassword" placeholder="Password"  v-model="password">
                <label for="floatingPassword">Password</label>
            </div> </div>

            <div class="container d-flex justify-content-center">
                <a class="nav-link" @click="register()" style="font-size: 27px; cursor: pointer;">Register</a>
            </div>
        </form>
        </div>
    </main>
</template>
<script>
import {requestRegistration} from '../../services/userService';

export default {
    name: 'RegistrationForm',
    data() {
        return {
            firstname: '',
            lastname: '',
            bsn: '',
            phone: '',
            email: '',
            password: ''
        };
    },
        methods:{
            async register() {
            const userData = {
                firstName: this.firstname,
                lastName: this.lastname,
                bsnNumber: this.bsn,
                phoneNumber: this.phone,
                email: this.email,
                password: this.password
            };

            try {
                const response = await requestRegistration(userData);
                console.log(response);
                alert('Registration successful!');
            } catch (error) {
                console.error('Error:', error);
                alert('Registration failed.');
            }
        }
        

    }
}
    
</script>

<style>
form{
    padding-top: 5%;
    
    input {
        border: 2px solid #942EE5 !important;
    }

    a{
        color: #942EE5 !important;
        border: 2px solid #942EE5 !important;
        border-radius: 20% !important;
        font-size: 16px !important;
        padding: 2% !important;
        margin: 5% !important;
    }

    a:hover{
        color: #9574b1 !important;
        border-color: #4e0789 !important;
    }
}
</style>
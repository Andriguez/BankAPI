<script setup>
import UsersDetails from '../users_details.vue'
import {getUserById} from '@/services/userService'

</script>

<template>
<form class="row g-3 mx-5 pt-5" style="max-width: 700px;">
  <div class="d-flex">
    <UsersDetails :details="userdetails"/>
  </div>
  <div class="col-12">
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Open Account</a>
  </div>
</form>
</template>

<script>
export default {
    name: 'RegistrationOverview',
    data(){
      return {
        userdetails: []
      }
    },
    props: {
      id: Number
    },
    async mounted() {
        try{

          const response = await getUserById(this.id);
          this.userdetails = response;

        } catch(error) {
        console.log(error)
        }
      
    }
}
</script>

<style>
label{
  color: white;
}
</style>
<script setup>
import UsersDetails from '../users_details.vue'
import {getUserById} from '@/services/userService'

</script>

<template>
    <UsersDetails :details="userdetails" :userId="id"/>
</template>

<script>    
export default {
    name: 'UsersOverview',
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
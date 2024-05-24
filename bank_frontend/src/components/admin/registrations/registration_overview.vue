<script setup>
import UsersDetails from '../users_details.vue'
</script>

<template>
    <UsersDetails :userId="id"/>
</template>

<script>
export default {
    name: 'RegistrationOverview',
    data(){
      return {
      }
    },
    props: {
      id: Number
    }
}
</script>

<style>
label{
  color: white;
}
</style>
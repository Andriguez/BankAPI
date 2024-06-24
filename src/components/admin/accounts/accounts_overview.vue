<script setup>
import AccountsDetails from './accounts_details.vue'

</script>

<template>
    <AccountsDetails :userId="id"/>
</template>

<script>    
export default {
    name: 'AccountsOverview',
    data(){
      return {
        
      }
    },
    props: {
      id: Number
    },
}
</script>

<style>
label{
  color: white;
}
</style>
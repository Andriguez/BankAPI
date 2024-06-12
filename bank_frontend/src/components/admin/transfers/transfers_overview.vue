<script setup>
import TransferPage from '../../transfers/transferPage.vue'

</script>

<template>
    <TransferPage :userId="id"/>
</template>

<script>    
export default {
    name: 'TransfersOverview',
    data(){
      return {
      }
    },
    props: {
      id: Number
    },
    mounted() {
      this.user = this.id;
    }
}
</script>

<style>
label{
  color: white;
}
</style>
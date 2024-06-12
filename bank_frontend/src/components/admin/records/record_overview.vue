<script setup>
import RecordDetails from './record_details.vue'

</script>

<template>
    <RecordDetails :userId="id"/>
</template>

<script>    
export default {
    name: 'RecordOverview',
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
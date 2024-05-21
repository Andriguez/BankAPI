<script setup>
import RegistrationItem from './registration_item.vue'
import { getRegistrations } from '../../../services/userService.js'
</script>
<template>
<div id="registrations-table" class="d-flex flex-column p-3 text-bg-dark" style="width: 400px; height: 450px;">
      <span class="fs-4">Registrations</span>
    <hr>
    <ul class="nav nav-pills flex-column">
      <RegistrationItem v-for="registration in registrations"
      :key = "registration.id"
      :registration = "registration"
      />

    </ul>
  </div>
</template>

<script>
export default {
    name: 'RegistrationsTable',
    data(){
        return {
            registrations: []
        }
    },
    methods: {
        getAllRegistrations(){
            try{
                registrations = getRegistrations()
                console.log(registrations)
            } catch(error){
                console.log(error)
            }
        }
    },
    mounted(){
        getRegistrations()
        .then((data) =>{ this.registrations = data })
        .catch((error) => console.log(error));
    }

}
</script>

<style>
#registrations-table{
    overflow: auto !important;
    
}
</style>
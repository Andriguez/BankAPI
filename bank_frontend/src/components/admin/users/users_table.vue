<script setup>
import UsersItem from '../users_item.vue'
import { getUsers } from '../../../services/userService.js'
</script>
<template>
<div id="users-table" class="d-flex flex-column p-3 text-bg-dark" style="width: 400px; height: 100vh;">
      <span class="fs-4">Users</span>
    <hr>
    <ul class="nav nav-pills flex-column">
      <UsersItem 
       v-for="user in users"
       :key = "user.id"
       :registration = "user"
      />

    </ul>
  </div>
</template>

<script>
export default {
    name: 'UsersTable',
    data(){
        return {
            users: []
        }
    },
    methods: {
        getAllUsers(){
            try{
                registrations = getUsers()
                console.log(users)
            } catch(error){
                console.log(error)
            }
        }
    },
    mounted(){
        getUsers()
        .then((data) =>{ this.users = data })
        .catch((error) => console.log(error));
    }

}
</script>

<style>
#users-table{
    overflow: auto !important;
    
}
</style>
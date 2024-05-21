<script setup>
import {getUserById} from '../../../services/userService'
</script>
<template>

<li class="nav-item">
    <!--use class active to display the selected one-->
        <a href="#" class="nav-link text-white" aria-current="page" @click="getUserById(registration.Id)">
          {{ registration.firstName }} {{ registration.lastName }}
        </a>

    </li>

</template>

<script>
export default {
name: 'RegistrationItem',
props: {
    registration: Object
},
methods: {
    getUserInfo(id){
     try{
        getUserById(id)
        } catch(error) {
        console.log(error)
        }
}
}
}
</script>

<style>
span{
    float: right;
}

.nav-item:hover{
 background-color: #6504c6;
}
</style>
<script setup>
import UsersDetails from '../users_details.vue'
</script>

<template>
<form class="row g-3 mx-5 pt-5" style="max-width: 700px;">
  <div class="d-flex">
    <UsersDetails/>
  </div>
  <div class="col-12">
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Open Account</a>
  </div>
</form>
</template>

<script>
export default {
    name: 'RegistrationOverview'
}
</script>

<style>
label{
  color: white;
}
</style>
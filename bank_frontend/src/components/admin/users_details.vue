<template>
<form class="row g-3 mx-5 pt-5" style="max-width: 700px;">
  <div class="col-6">
    <label for="inputEmail4" class="form-label">Email</label>
    <input type="email" class="form-control" id="inputEmail4" :value="details.email" readonly>
  </div>
  <div class="col-4">
    <label for="inputBsn" class="form-label">BSN</label>
    <input type="number" class="form-control" id="inputBsn" :value="details.bsnNumber" readonly>
  </div>
  <div class="col-4">
    <label for="inputFirstName" class="form-label">First Name</label>
    <input type="text" class="form-control" id="inputFirstName" :value="details.firstName" readonly>
  </div>
  <div class="col-4">
    <label for="inputLastName" class="form-label">Last Name</label>
    <input type="text" class="form-control" id="inputLastName" :value="details.lastName" readonly>
  </div>
  <div class="col-md-4">
    <label for="inputPhone" class="form-label">Phone Number</label>
    <input type="number" class="form-control" :value="details.phoneNumber" id="inputPhone" readonly>
  </div>
  <div class="row g-3">
    <label>Current Account</label>
    <div class="col-md-4">
    <label for="inputPhone" class="form-label">Absolute Limit</label>
    <input type="number" class="form-control" id="currentAbsolute" :value="currentAbsolute">
  </div><div class="col-md-4">
    <label for="inputPhone" class="form-label">Daily Limit</label>
    <input type="number" class="form-control" id="currentLimit" :value="currentDaily">
  </div>
  </div>
  <div class="row g-3">
    <label>Savings Account</label>
    <div class="col-md-4">
    <label for="inputPhone" class="form-label">Absolute Limit</label>
    <input type="number" class="form-control" id="savingsAbsolute" :value="savingsAbsolute">
  </div><div class="col-md-4">
    <label for="inputPhone" class="form-label">Daily Limit</label>
    <input type="number" class="form-control" id="savingsLimit" :value="savingsDaily">
  </div>
  </div>
  <div v-if="$route.path == '/registrations'" class="col-12">
    <a class="nav-link p-2" @click="submitAccountsInfo" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Open Account</a>
  </div>

  <div v-if="$route.path == '/users'" class="col-12">
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Edit</a>
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Delete</a>
  </div>
</form>
</template>

<script>
import { createAccounts } from '@/services/accountsService';

export default {
    name: 'UsersDetails',
    data(){
      return {
        registration: '',
        currentAbsolute,
        savingsAbsolute,
        currentDaily,
        savingsDaily
      }
    },
    props: {
      details: Object,
      userId: Number
    },
    methods: {
      async submitAccountsInfo(){
        const currentInfo = {
          type: 'current',
          absolute: this.currentAbsolute,
          daily: this.currentDaily
        }

        const savingsInfo = {
          type: 'savings',
          absolute: this.savingsAbsolute,
          daily: this.savingsDaily
        }
        
        try{
          const reponse = await createAccounts(this.userId, currentInfo, savingsInfo);

          console.log(response)
          this.router.replace('/users')

        } catch (error){
          console.error(error)
        }
        
  }

    }
}
</script>

<style>
label{
  color: white;
}
</style>
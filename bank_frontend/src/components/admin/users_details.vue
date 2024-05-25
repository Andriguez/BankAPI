<template>
<form class="row g-3 mx-5 pt-5 mb-4" style="max-width: 700px;">
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
    <h4 style="color:white">Current Account</h4>
    <div class="col-md-4">
    <label for="currentAbsolute" class="form-label">Absolute Limit</label>
    <input type="number" class="form-control" id="currentAbsolute" v-model="currentAbsolute" :readonly="!hasUsertype('ADMIN')">
  </div><div class="col-md-4">
    <label for="currentDaily" class="form-label">Daily Limit</label>
    <input type="number" class="form-control" id="currentDaily" v-model="currentDaily" :readonly="!hasUsertype('ADMIN')">
  </div>
  </div>
  <div class="row g-3">
    <h4 style="color:white">Savings Account</h4>
    <div class="col-md-4">
    <label for="savingsAbsolute" class="form-label">Absolute Limit</label>
    <input type="number" class="form-control" id="savingsAbsolute" v-model="SavingsAbsolute" :readonly="!hasUsertype('ADMIN')">
  </div><div class="col-md-4">
    <label for="savingsDaily" class="form-label">Daily Limit</label>
    <input type="number" class="form-control" id="savingsDaily" v-model="SavingsDaily" :readonly="!hasUsertype('ADMIN')">
  </div>
  </div>
  <div v-if="$route.path == '/registrations' && hasUsertype('ADMIN')" class="col-12">
    <a class="nav-link p-2" @click="submitAccountsInfo" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Open Account</a>
  </div>

  <div v-if="$route.path == '/users' && hasUsertype('ADMIN')" class="col-12">
    <a class="nav-link p-2" @click="editAccountsInfo" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Edit</a>
    <a class="nav-link p-2" style="font-size: 18px; cursor: pointer; width: 150px; height: 50px; float: right; text-align: center;">Delete</a>
  </div>
</form>
</template>

<script>
import { createAccounts, editAccountsInfo } from '@/services/accountsService';
import {getUserById} from '@/services/userService'
import { useLoginStore } from '@/stores/loginStore';

export default {
    name: 'UsersDetails',
    data(){
      return {
        registration: '',
        details: Object,
        currentAbsolute: '',
        currentDaily: '',
        SavingsAbsolute: '',
        SavingsDaily: '',
        id: Number,
        loginStore: useLoginStore(),

      }
    },
    props: {
      userId: Number
    },
    methods: {
      async submitAccountsInfo(){
        const currentInfo = {
          absolute: this.currentAbsolute,
          daily: this.currentDaily,
          type: 'CURRENT'
        }

        const savingsInfo = {
          absolute: this.SavingsAbsolute,
          daily: this.SavingsDaily,
          type: 'SAVINGS'
        }
        
        try{
          const response = await createAccounts(this.userId, currentInfo, savingsInfo);

          console.log(response)
          this.$router.replace("/users");

        } catch (error){
          console.error(error)
        }
        
  },
    async editAccountsInfo(){
        const currentInfo = {
          absolute: this.currentAbsolute,
          daily: this.currentDaily,
          type: 'CURRENT'
        }

        const savingsInfo = {
          absolute: this.SavingsAbsolute,
          daily: this.SavingsDaily,
          type: 'SAVINGS'
        }
        
        try{
          const response = await editAccountsInfo(this.userId, currentInfo, savingsInfo);

          console.log(response)
          this.$router.replace("/users");

        } catch (error){
          console.error(error)
        }
        
  },
  hasUsertype(usertype){
          return this.loginStore.hasUsertype(usertype);
        }

    },
  async mounted(){
      try{

    if (this.userId == null){
      this.id = 0;
    } else {
      this.id = this.userId
    }

    const response = await getUserById(this.id);
    this.details = response;

    this.currentAbsolute = response.accountsInfo.CURRENT.absolute;
    this.currentDaily = response.accountsInfo.CURRENT.daily;

    this.SavingsAbsolute = response.accountsInfo.SAVINGS.absolute;
    this.SavingsDaily = response.accountsInfo.SAVINGS.daily;

    console.log(response.accountsInfo.CURRENT.absolute)
    console.log(this.currentAbsolute)

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
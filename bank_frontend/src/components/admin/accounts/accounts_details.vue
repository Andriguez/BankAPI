<template>
    <form class="row g-3 mx-5 pt-5 mb-4" style="max-width: 700px;">
      
      <div class="row g-3" v-for="(account, index) in accounts" :key="index">
        <h4 style="color:white">{{ account.type }} Account</h4>
        <div class="col-md-4">
        <label for="Iban" class="form-label">Iban</label>
        <input type="text" class="form-control" id="Iban" :value="account.iban" readonly>
      </div>
      <div class="col-md-4">
        <label for="currentBalance" class="form-label">Balance</label>
        <input type="number" class="form-control" id="Balance" :value="account.balance" readonly>
      </div>
      
        <div class="col-md-4">
        <label for="currentAbsolute" class="form-label">Absolute Limit</label>
        <input type="number" class="form-control" id="Absolute" :value="account.absoluteLimit" readonly>
      </div>
      <div class="col-md-4">
        <label for="currentDaily" class="form-label">Daily Limit</label>
        <input type="number" class="form-control" id="Daily" :value="account.dailyLimit" readonly>
      </div>
      <div class="col-md-4">
        <label for="currentStatus" class="form-label">Account Status</label>
        <input type="text" class="form-control" id="Status" :value="account.accountStatus" readonly>
      </div>
      </div>

    </form>
    </template>
    
    <script>
   import {getAccountsById} from '@/services/accountsService'
   import { useLoginStore } from '@/stores/loginStore';

    export default {
    name: 'AccountsDetails',
    data() {
        return {
            loginStore: useLoginStore(),
            name: "",
            role: "",
            accounts: [],
            hasAccounts: false,
            errorMsg: "",
            id: Number
        };
    },
    props: {
      userId: Number
    },
    async mounted(){
      try{

    if (this.userId == null){
      this.id = 0;
    } else {
      this.id = this.userId;
    }

    const response = await getAccountsById(this.id);
    this.accounts = response;
    console.log(this.accounts)

    } catch(error) {
    console.log(error)
    }
    }
};

 
    </script>
    
    <style>
    label{
      color: white;
    }
    </style>
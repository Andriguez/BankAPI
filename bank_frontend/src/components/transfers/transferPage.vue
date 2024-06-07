<template>
<div class="container d-flex flex-nowrap m-0 p-0" >

<div class="container m-5 px-5 d-flex flex-wrap">
    <div class="container d-flex" style="color: white;">
<svg xmlns="http://www.w3.org/2000/svg" width="50" height="50" fill="currentColor" class="bi bi-arrow-left-right mx-2" viewBox="0 0 16 16">
  <path fill-rule="evenodd" d="M1 11.5a.5.5 0 0 0 .5.5h11.793l-3.147 3.146a.5.5 0 0 0 .708.708l4-4a.5.5 0 0 0 0-.708l-4-4a.5.5 0 0 0-.708.708L13.293 11H1.5a.5.5 0 0 0-.5.5m14-7a.5.5 0 0 1-.5.5H2.707l3.147 3.146a.5.5 0 1 1-.708.708l-4-4a.5.5 0 0 1 0-.708l4-4a.5.5 0 1 1 .708.708L2.707 4H14.5a.5.5 0 0 1 .5.5"/>
</svg>
<h1>Bank Transfer</h1>
</div>

<div class="container d-flex m-3">
    <h1 style="font-size: 80px;">€</h1>
    <input type="number" min="1" class="amount-btn" placeholder="Amount">
</div>
<div class="container my-3" style="color: white;">
    <h2>Transfer from:</h2>
    <div class="container d-flex">
    <select class="mx-3" aria-label="Default select example" style="width: 100%; height: 50px; font-size: 30px;" v-for="(account, index) in accounts" :key="index">
        <option selected>{{ account.type }}: {{account.iban}} [ €{{ account.balance }} ]</option>
</select>
</div>
</div>
<div class="container my-3" style="color: white;">
    <h2>Transfer to:</h2>
    <div class="container d-flex">
        <input type="text" class="mx-3" id="fullnameInput" placeholder="Full Name" style="width: 40%; font-size: 25px;" required>
        <input type="text" class="mx-3" id="ibanInput" placeholder="IBAN" style="width: 40%; font-size: 25px;" required>
        <button class="transfer-btn" style="width: 100px !important; height: 50px !important; font-size: 20px;">Verify</button>
    </div>

</div>

<div class="container m-4 d-flex justify-content-center">
    <button class="transfer-btn"><h1>Transfer</h1></button>
</div>

</div>
</div>
</template>

<script setup>
import { getAccountsById } from '@/services/accountsService';
import UsersTable from '../admin/users/users_table.vue'
import { useLoginStore } from '@/stores/loginStore';
</script>

<style>
.transfer-btn{
  padding: 7px;
          margin: 5px;
          background-color: #201F1F;
          border: solid 3px #47008F;
          border-radius: 16px;
          height: 90px !important;
          width: 250px !important;

          font-size: 52px;
          color: #6504c6;
}
</style>



<script>
export default {
    name: 'TransferPage',
    data(){
        return{
            //userId: Number,
            loginStore: useLoginStore(),
            accounts: []
        }
    },
    props:{
        userId: Number,
    },
    methods: {
        setUserId(id){
            this.$nextTick(() => {
                this.userId = id;
            });

        },
        hasUsertype(usertype){
          return this.loginStore.hasUsertype(usertype);
        },

        
    },
    async mounted(){
      try{
        console.log(this.userId);


    const response = await getAccountsById(this.userId);
    this.accounts = response.data;
    console.log(response)

    } catch(error) {
        console.log(error)
        }
    }
    
}
</script>
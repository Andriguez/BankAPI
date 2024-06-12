<template>
    <div class="container d-flex justify-content-center m-5">
      <input type="number" min="10" step="5" class="other-amount-btn" placeholder="Other Amount" v-model="amount">

       <button class="amount-btn" @click="selectAmount(10)"><h3>€10</h3></button>
       <button class="amount-btn" @click="selectAmount(20)"><h3>€20</h3></button>
       <button class="amount-btn" @click="selectAmount(50)"><h3>€50</h3></button>
       <button class="amount-btn" @click="selectAmount(100)"><h3>€100</h3></button>


    </div>
    <button class="confirm-btn m-3" @click="createATMTransaction()"><span>Confirm</span></button>
    <button class="back-btn m-3" @click="selectBtn('main')"><span>Go Back</span></button>


</template>

<script>
import { createTransaction } from '@/services/transactionsService';
import { useUserAccountStore } from "@/stores/userAccountStore";

export default {
   name: 'ATMamount',
   emits: ['btn-selected'],
   data(){
      return {
         amount: Number,
         transactionType: '',
         atmAccount: 'NLXXINHOXXXXXXXXXX',
         senderAcc: '',
         receiverAcc: '',
         
      }
   },
   props: {
      type: String
   },
   methods: {
        selectBtn(window){
            this.$emit('btn-selected', window, '');
        },
        selectAmount(selectedAmount){
            this.amount = selectedAmount;
        },
        async createATMTransaction(){
            try{
               await this.setAccounts(this.type);

               const transactionData = {
               sender: this.senderAc,
               receiver: this.receiverAc,
               amount: this.amount,
               type: this.type
               }

            console.log(transactionData)

            const response = await createTransaction(transactionData);

            console.log(response);
                     } catch(error){
                        console.error(error);
                        alert(error);
                        }

                        this.selectBtn('main')
            
        },

      async setAccounts(type){
         const userAccounts = this.accountStore.userCurrentAccount;
         //console.log(userAccounts.iban);

            switch(type) {
               case 'WITHDRAWAL':
                  this.senderAc = userAccounts.iban,
                  this.receiverAc = this.atmAccount
                  break;
               case 'DEPOSIT':
                  this.senderAc = this.atmAccount,
                  this.receiverAc = userAccounts.iban
                  break;
               default:
                  this.sender = '',
                  this.receiver= ''
            }
        }
      },
      async mounted(){
         this.accountStore = await useUserAccountStore();
         await this.accountStore.getUserAccounts();

      }
}
</script>

<style>
.amount-btn{
   padding: 7px;
           margin: 5px;
           background-color: #201F1F;
           border: solid 3px #47008F;
           border-radius: 16px;
           height: 90px !important;
           width: 150px !important;

           font-size: 22px;
           color: #6504c6;
}

.other-amount-btn{
   padding: 7px;
           margin: 5px;
           background-color: #201F1F;
           border: solid 3px #47008F;
           border-radius: 16px;
           height: 90px !important;
           width: 250px !important;

           font-size: 32px;
           color: #6504c6;
}

.back-btn{
   padding: 7px;
           margin: 10px;
           background-color: #201F1F;
           border: solid 3px #E52E44;
           border-radius: 16px;
           height: 100px;
           width: 150px;
           float: right;

           font-size: 22px;
           color: #E52E44;
}

.confirm-btn{
   padding: 7px;
           margin: 10px;
           background-color: #201F1F;
           border: solid 3px #047616;
           border-radius: 16px;
           height: 100px;
           width: 150px;
           float: right;

           font-size: 22px;
           color: #047616;
}

.amount-btn:hover, .selected{
   background-color: #8e6daf;
}

.back-btn:hover {
   background-color: #d37680;
}

.confirm-btn:hover {
   background-color: #66ab70;
}
</style>
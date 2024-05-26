<script setup>
</script>

<template>
    <div class="container d-flex justify-content-center flex-wrap">
        <div class="container" style="text-align: center;">
        <h1 class="m-3">{{ loginStore.name }}'s Account(s)</h1>
        </div>
        <div class="container account" v-for="account in userAccounts">
            <h3>{{ account.type }} Account</h3>
            <h5>IBAN: {{ account.iban }} </h5>
            <h2 style="color: white;">€ {{ account.balance }}</h2>
        </div>
    </div>
    <button class="back-btn m-3" @click="selectBtn('main')"><span>Go Back</span></button>


</template>
<script>
import { useLoginStore } from '@/stores/loginStore';
import { getAccountsOfCustomer } from '@/services/accountsService';
    export default {
        name: 'ATMbalance',
        emits: ['btn-selected'],
        data(){

            return {
                loginStore: useLoginStore(),
                userAccounts: []
            }
        },
    methods: {
        selectBtn(){
            this.$emit('btn-selected', 'main');
        }
    },
    async mounted(){
        this.userAccounts = await getAccountsOfCustomer();
    }
    }
</script>

<style>
.account{
    border: solid 2px white;
    margin: 20px;
    text-align: center;
    width: 600px;
    padding: 10px;
}
</style>
<template>
    <div class="account_view">
        <h1> {{ name }}, our dear {{ role }} </h1>
        <div class="d-flex" v-if="hasAccounts">
            <div>
                <h1>Accounts</h1>
                <div class="oneAccount" v-for="(account, index) in accounts" :key="index">
                    <p>IBAN: {{ account.iban }}</p>
                    <p>Balance: € {{ account.balance }}</p>
                    <p>Type: {{ account.type }}</p>
                </div>
                <p>Total Balance: € {{ totalBalance }}</p>
            </div>

            <p class="text-danger errorMsg">{{ errorMsg }}</p>
        </div>
        <div class="d-flex" v-if="!hasAccounts">
            <p> you don't have any accounts yet.</p>
        </div>
    </div>

</template>

<script>
import { useLoginStore } from '../stores/loginStore';
import { getAccountsOfCustomer } from '../services/accountsService';

export default {
    name: 'Accounts',
    data() {
        return {
            loginStore: useLoginStore(),
            name: "",
            role: "",
            jwtToken: "",
            accounts: [],
            hasAccounts: false,
            totalBalance: 0,
            errorMsg: "",
        };
    },
    mounted() {
        this.name = this.loginStore.name;
        this.role = this.loginStore.usertype;
        this.jwtToken = this.loginStore.jwtToken;
        this.getAllAccounts();
    },
    methods: {
        async getAllAccounts() {
            try {
                let accounts = await getAccountsOfCustomer(this.jwtToken);
                console.log(accounts);
                console.log(accounts.length);
                this.hasAccounts = accounts.length > 0;
                this.accounts = accounts;
                accounts.forEach(account => {
                    this.totalBalance += account.balance;
                })
            } catch (error) {
                this.accounts = [];
            }
        },
    },
};

</script>

<style>
.container {
    color: blueviolet;
}
</style>

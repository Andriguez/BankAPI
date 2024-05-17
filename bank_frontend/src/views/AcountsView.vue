<template>

    <h1>Account of user with username: {{ username }}</h1>
    <div class="d-flex" v-if="hasAccounts">
        <div class="detailCard">
            <label>Accounts</label>
            <span type="text">{{ 1 }}</span>
        </div>
        <div class="detailCard">
            <label>Total Balance</label>
            <span type="text">€ {{ totalBalance }}</span>
        </div>
        <p class="text-danger errorMsg">{{ errorMsg }}</p>
    </div>
    <div class="d-flex" v-if="!hasAccounts">
        <p> you don't have any accounts yet.</p>
    </div>


</template>

<script>
import { useLoginStore } from '../stores/loginStore';
import { getAccountsByUsername } from '../services/accountsService';

export default {
    name: 'Accounts',
    data() {
        return {
            loginStore: useLoginStore(),
            username: "",
            jwtToken: "",
            accounts: [],
            hasAccounts: false,
            totalBalance: 0,
            errorMsg: "",
        };
    },
    mounted() {
        this.username = this.loginStore.username;
        this.jwtToken = this.loginStore.jwtToken;
        this.getAllAccounts();
    },
    methods: {
        async getAllAccounts() {
            try {
                let accounts = await getAccountsByUsername(this.username, this.jwtToken);
                console.log(accounts);
                console.log(accounts.length);
                // this.hasAccounts = accounts.length > 0;
            } catch (error) {
                this.accounts = [];
            }
        },
    },
};

</script>

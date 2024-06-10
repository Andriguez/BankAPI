<template>
    <div class="account_view">
        <div class="d-flex" v-if="hasAccounts">
            <div class="container" style="color: black">
                <h1 class="m-3" style="color: #6504c6">My Accounts</h1>
                <button class="oneAccount mx-5" v-for="(account, index) in accounts" :key="index"
                    @click="openAccountTransactions(account.type)">
                    <p style="float: left; font-size: 22px;">{{ account.type }}</p>
                    <br>
                    <br>
                    <p style="float: left; font-size: 20px">IBAN: {{ account.iban }}</p>
                    <p style="float: right; font-size: 23px;">Balance: € {{ account.balance }}</p>
                </button>
                <h2 class="m-4" style="color: white;">Total Balance: € {{ totalBalance }}</h2>
            </div>

            <p class="text-danger errorMsg">{{ errorMsg }}</p>
        </div>
        <div class="d-flex" v-if="!hasAccounts">
            <p> you don't have any accounts yet.</p>
        </div>

        <div class="container m-4">
            <button class="amount-btn mb-4" style="float: right;" @click="openTransferPage">
                <h1>Transfer</h1>
            </button>
        </div>
    </div>

</template>
<script>
import { useLoginStore } from '@/stores/loginStore';
import { getAccountsOfCustomer } from '@/services/accountsService';

export default {
    name: 'AccountsPage',
    data() {
        return {
            loginStore: useLoginStore(),
            name: "",
            role: "",
            accounts: [],
            hasAccounts: false,
            totalBalance: 0,
            errorMsg: "",
        };
    },
    mounted() {
        this.name = this.loginStore.name;
        this.role = this.loginStore.usertype;
        this.getAllAccounts();
    },
    methods: {
        async getAllAccounts() {
            try {
                let response = await getAccountsOfCustomer();
                console.log(response);

                let accounts = response.accounts;

                this.accounts = Object.values(accounts);
                this.hasAccounts = this.accounts.length > 0;

                this.accounts.forEach(account => {
                    this.totalBalance += account.balance;
                })
            } catch (error) {
                this.accounts = [];
            }
        },

        openAccountTransactions(type) {

            let lowercaseType = "";

            if (type === "CURRENT"){
                lowercaseType = "current";
            } else if (type === "SAVINGS"){
                lowercaseType = "savings";
            }

            this.$router.replace(`/transactions/${lowercaseType}`)
        },

        openTransferPage() {
            this.$router.replace('/transfer')
        }
    },
};

</script>

<style>
.account_view {
    color: black;
}

.oneAccount {
    background-color: white;
    border-radius: 20px;
    padding: 20px;
    margin-bottom: 10px;
    width: 80%;
}
</style>

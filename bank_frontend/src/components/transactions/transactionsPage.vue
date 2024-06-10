<template>
    <div class="transaction_view">
        <div class="container mx-5 mt-5 mb-3" style="color: white;">
            <span style="font-size: 30px;"> Showing {{ type }} account of {{ name }}</span>
            <br>
            <br>
            <span class="mx-5" style="font-size: 35px;">IBAN → {{ account?.iban }}</span>
            <span class="me-5" style="font-size: 35px; float: right;">€ {{ account?.balance }}</span>
            <br>
        </div>
        <div class="container mx-5 mt-5 mb-3">
            <table>
                <tr>
                    <td><label for="transaction-type">Transaction Type:</label></td>
                    <td>
                        <select id="transaction-type" v-model="transactionType">
                            <option value="all">All</option>
                            <option value="TRANSFER">Transfer</option>
                            <option value="DEPOSIT">Deposit</option>
                            <option value="WITHDRAWAL">Withdrawal</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td><label for="start-date">Start Date:</label></td>
                    <td><input id="start-date" v-model="startDate" type="date"></td>
                    <td><label for="end-date">End Date:</label></td>
                    <td><input id="end-date" v-model="endDate" type="date"></td>
                </tr>
                <tr>
                    <td><label for="min-amount">Min amount:</label></td>
                    <td><input id="min-amount" v-model="minAmount" type="number"></td>
                    <td><label for="max-amount">Max amount:</label></td>
                    <td><input id="max-amount" v-model="maxAmount" type="number"></td>
                </tr>
                <tr>
                    <td><label for="exact-amount">Exact amount:</label></td>
                    <td><input id="exact-amount" v-model="exactAmount" type="number"></td>
                    <td><label for="iban">IBAN:</label></td>
                    <td><input id="iban" v-model="iban" type="text"></td>
                </tr>
                <tr>
                    <td colspan="4" style="text-align: center;"><button @click="filterTransactions">Filter</button></td>
                </tr>
            </table>
        </div>


        <div class="container mx-5 mt-5 mb-3" v-if="hasTransactions">
            <div class="container" style="color: black">
                <h1 class="m-3" style="color: #6504c6">Transactions</h1>
                <table class="table table-bordered mx-5">
                    <thead>
                        <tr>
                            <th>Transaction Type</th>
                            <th>Sender</th>
                            <th>Receiver</th>
                            <th>Date</th>
                            <th>Amount (€)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(transaction, index) in transactions" :key="index">
                            <td style="font-size: 22px;">{{ transaction.transactionType }}</td>
                            <td>{{ transaction?.senderAccount?.iban }}</td>
                            <td>{{ transaction?.receiverAccount?.iban }}</td>
                            <td style="font-size: 20px;">{{ formatDate(transaction.dateTime) }}</td>
                            <td style="font-size: 23px;">{{ transaction.amount }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>


            <p class="text-danger errorMsg">{{ errorMsg }}</p>
        </div>
        <div class="container mx-5 mt-5 mb-3" v-if="!hasTransactions">
            <h4> No transactions found.</h4>
        </div>

    </div>

</template>

<script>
import { useLoginStore } from '@/stores/loginStore';
import { getTransactionOfCustomerByType } from '@/services/transactionsService';
export default {
    name: 'TransactionPage',
    props: ['type'],
    data() {
        return {
            loginStore: useLoginStore(),
            name: "customer",
            account: null,
            transactions: [],
            hasTransactions: false,
            transactionType: null,
            startDate: null,
            endDate: null,
            minAmount: null,
            exactAmount: null,
            maxAmount: null,
            iban: null,
            errorMsg: "",
        };
    },
    mounted() {
        this.name = this.loginStore.name;
        this.getAllTransactionsWithType();
    },
    methods: {
        async getAllTransactionsWithType() {
            try {
                let accountsTransactions = await getTransactionOfCustomerByType(this.type, this.transactionType, this.startDate, this.endDate, this.minAmount, this.exactAmount, this.maxAmount, this.iban);
                console.log(accountsTransactions);
                let account = accountsTransactions.account;
                let transactions = accountsTransactions.transactions;
                console.log(account);
                console.log(transactions);
                console.log(transactions.length);
                this.hasTransactions = transactions.length > 0;
                this.transactions = transactions;
                this.account = account;
            } catch (error) {
                this.transactions = [];
            }
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        },
        async filterTransactions() {
            console.log(this.transactionType);
            console.log(this.startDate);
            console.log(this.endDate);
            console.log(this.minAmount);
            console.log(this.exactAmount);
            console.log(this.maxAmount);
            console.log(this.iban);
            this.getAllTransactionsWithType();
        }
    },
}

</script>


<style>
.transaction_view {
    color: white;
}

.transaction {
    background-color: white;
    color: black;
    width: 40rem;
    font-size: 20px;
    border: solid 2px black;
}

.date {
    font-size: 25px;
}

.withdrawal {
    color: red;
}

.deposit {
    color: green;
}

#history-container {
    max-height: 20rem;
    width: 50rem;
    overflow-y: auto;
}

.filter {
    padding: 7px;
    margin: 5px;
    background-color: #201F1F;
    border: solid 3px #47008F;
    border-radius: 16px;
    height: 60px !important;
    width: 120px !important;

    font-size: 25px;
    color: #6504c6;
}
</style>
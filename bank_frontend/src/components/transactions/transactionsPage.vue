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


        <div class="container mx-5 mt-5 mb-3">
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
                            <td>{{ transaction.transactionType }}</td>
                            <td>{{ transaction?.senderAccount?.iban }}</td>
                            <td>{{ transaction?.receiverAccount?.iban }}</td>
                            <td>{{ formatDate(transaction.dateTime) }}</td>
                            <td>{{ transaction.amount }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="pagination-controls">
                <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
                <span>Page {{ currentPage }}</span>
                <button @click="nextPage">Next</button>
            </div>

            <p class="text-danger errorMsg">{{ errorMsg }}</p>
        </div>
    </div>

</template>

<script>
import { useLoginStore } from '@/stores/loginStore';
import { useTransactionStore } from '@/stores/transactionStore';
import { getTransactionOfCustomerByType } from '@/services/transactionsService';
import { formatDate } from '@/services/helpers';
export default {
    name: 'TransactionPage',
    props: ['type'],
    data() {
        return {
            loginStore: useLoginStore(),
            transactionStore: useTransactionStore(),
            name: "customer",
            account: null,
            transactions: [],
            currentPage: 1,
            pageSize: 10,
            skip: 0,
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
    // the following watch says when the type changes call getAllTransactionsWithType.
    watch: {
        type: 'getAllTransactionsWithType'  // Watch the 'type' prop and call the method on change
    },
    methods: {
        formatDate,
        async getAllTransactionsWithType() {
            try {
                this.skip = (this.currentPage - 1) * this.pageSize;
                this.limit = this.pageSize;
                await this.transactionStore.getTransactionOfCustomerByType(this.type, this.transactionType, this.startDate, this.endDate, this.minAmount, this.exactAmount, this.maxAmount, this.iban, this.limit, this.skip);
                this.account = this.transactionStore.getAccountInfo;
                this.transactions = this.transactionStore.getTransactionsList;                
            } catch (error) {
                this.transactions = [];
            }
        },
        nextPage() {
            this.currentPage++;
            this.getAllTransactionsWithType();
        },
        prevPage() {
            if (this.currentPage > 1) {
                this.currentPage--;
                this.getAllTransactionsWithType();
            }
        },
        async filterTransactions() {
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
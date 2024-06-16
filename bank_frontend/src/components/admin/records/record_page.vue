<script setup>
import UsersTable from '../users/users_table.vue';
import { useTransactionStore } from '@/stores/transactionStore';
import { formatDate } from '@/services/helpers';
</script>

<template>
    <div class="d-flex">
    <div class="d-flex">
        <UsersTable @selectUser="setUserId"/>
    </div>
    <div class="container h-25 w-10 p-3">
        <button @click="setCondition('ALL')">All Transactions</button>
        <button @click="setCondition('ATM')">ATM transactions</button>
        <button @click="setCondition('ADMIN')">Employee Transactions</button>
        <button @click="setCondition('CUSTOMER')">Customer Transactions</button>
        
        <div v-if="transactions.length > 0" class="container mx-5 mt-5 mb-3 w-auto">
            <div class="container" style="color: black">
                <h1 style="color: #6504c6"> {{condition}} Transactions</h1>
                <table  class="table table-bordered">
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
                        <tr v-for="(transaction) in transactions">
                            <td>{{ transaction.transactionType }}</td>
                            <td>{{ transaction.senderAccount?.iban }}</td>
                            <td>{{ transaction.receiverAccount?.iban }}</td>
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
        </div>
        <div v-else>
        <h3 style="color: #6504c6" class="m-3">There are no transactions displayed</h3>
        </div>
    
       
    </div>
    </div>
</template>

<script>

export default {
    name: 'RecordPage',
    data(){
        return{
            userId: 0,
            condition: "ALL",
            transactionStore: useTransactionStore(),
            transactions: [],
            currentPage: 1,
            pageSize: 10,
            skip: 0,
        }
    },
    methods: {
        formatDate,
        setUserId(id){
            this.$nextTick(() => {
                this.userId = id;
                this.setCondition("ID");
            });

        },  setCondition(condition){            
            this.condition = condition;
            if (condition != "ID") {
            this.userId = null; // Set userId to null if condition is "ID"
    }
    this.getFilteredTransactions();

        },  async getFilteredTransactions() {
            try {
                this.skip = (this.currentPage - 1) * this.pageSize;
                this.limit = this.pageSize;
                const response = await this.transactionStore.filterTransactions(this.condition, this.userId, this.skip, this.limit);
                this.transactions = response;
                console.log(response)              
            } catch (error) {
                this.transactions = [];
            }
        },
        
        nextPage() {
            this.currentPage++;
            this.getFilteredTransactions();
        },
        prevPage() {
            if (this.currentPage > 1) {
                this.currentPage--;
                this.getFilteredTransactions();
            }
        }
    }
}
</script>

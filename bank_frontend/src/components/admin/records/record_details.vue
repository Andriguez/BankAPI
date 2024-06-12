<template>
        <div class="container mx-5 mt-5 mb-3" v-if="hasTransactions">
            <div class="container" style="color: black">
                <h1 class="m-3" style="color: #6504c6">Transactions</h1>
                <button class="oneTransaction mx-5" v-for="(transaction, index) in transactions" :key="index">
                    <p style="float: left; font-size: 22px;">{{ transaction.transactionType }}</p>
                    <br>
                    <p> 
                        Sender: {{ transaction?.senderAccount?.iban }}
                    </p>
                    <p> 
                        Receiver: {{ transaction?.receiverAccount?.iban }}
                    </p>
                    <br>
                    <p style="float: left; font-size: 20px">Date: {{ transaction.dateTime }}</p>
                    <p style="float: right; font-size: 23px;">Amount: € {{ transaction.amount }}</p>
                </button>
            </div>
        </div>
        <div class="container mx-5 mt-5 mb-3" v-if="!hasTransactions">
            <h4> you don't have any transactions for this account yet.</h4>
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
            transactions: [],
        };
    },
    mounted() {
        this.name = this.loginStore.name;
    },
    methods: {
        
            
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
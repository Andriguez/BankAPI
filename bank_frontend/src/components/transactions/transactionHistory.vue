<script setup>
import TransactionDateBox from '@/components/transactions/transactionDateBox.vue'
</script>

<template>
    <div>
        <TransactionDateBox />
    </div>

    <div>
        <TransactionDateBox />
    </div>

    <div>
        <TransactionDateBox />
    </div>
</template>

<script>
export default {
    name: 'TransactionHistory',

}
</script>
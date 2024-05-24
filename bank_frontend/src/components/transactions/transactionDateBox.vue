<script setup>
import Transaction from '@/components/transactions/transaction.vue'
</script>

<template>
        <span class="date">Tuesday 07/05/24</span>
        <Transaction/>
        <Transaction/>
        <Transaction/>
</template>

<script>
export default {
    name: 'TransactionDateBox',

}
</script>
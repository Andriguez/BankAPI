<script setup>
import TransactionsPage from '@/components/transactions/transactionsPage.vue'
</script>

<template>
    <TransactionsPage :type="$route.params.type" />
</template>


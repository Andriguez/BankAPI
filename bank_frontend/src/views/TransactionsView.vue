<!-- <script setup>
import TransactionsPage from '@/components/transactions/transactionsPage.vue'
</script>

<template>
    <TransactionsPage :type="$route.params.type" />
</template> -->



<script setup>
import TransactionsPage from '@/components/transactions/transactionsPage.vue'
import RecordPage from '@/components/admin/records/record_page.vue'
import { useLoginStore } from '@/stores/loginStore';

</script>
<template>
    <div v-if="loginStore.hasUsertype('ADMIN')">
        <RecordPage/>
    </div>
<TransactionsPage v-else="loginStore.hasUsertype('CUSTOMER')" :type="$route.params.type"  />
</template>

<script>
export default {
    data() {
        return {
            loginStore: useLoginStore(),
        };
    }
}

</script>
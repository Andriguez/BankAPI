<script setup>
import TransferPage from '@/components/transfers/transferPage.vue'
import AdminTransferPage from '@/components/admin/transfers/transfers_page.vue'
import { useLoginStore } from '@/stores/loginStore';
</script>
<template>
    <div v-if="loginStore.hasUsertype('ADMIN')">
        <AdminTransferPage/>
    </div>
<TransferPage v-else="loginStore.hasUsertype('CUSTOMER')" />
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
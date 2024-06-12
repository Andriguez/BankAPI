<script setup>
import UsersTable from '../users/users_table.vue'
import RecordOverview from './record_overview.vue'
</script>

<template>
    <div class="d-flex">
    <div class="d-flex">
        <UsersTable @selectUser="setUserId"/>
    </div>
    <div class="container h-25 w-10 p-3">
        <button>All Transactions</button>
        <button>ATM transactions</button>
        <button>Employee Transactions</button>
        <button>Customer Transactions</button>

        <RecordOverview v-if="userId !== 0" :id="userId"/>
    </div>
    </div>
</template>

<script>
export default {
    name: 'RecordPage',
    data(){
        return{
            userId: 0
        }
    },
    methods: {
        setUserId(id){
            this.userId = 0;

            this.$nextTick(() => {
                this.userId = id;
            });

        }
    }
}
</script>

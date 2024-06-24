<script setup>
import UsersTable from '../users/users_table.vue'
import TransfersOverview from './transfers_overview.vue'
</script>

<template>
    <div class="d-flex">
        <UsersTable @selectUser="setUserId"/>
        <TransfersOverview v-if="userId !== 0" :id="userId"/>
    </div>
</template>

<script>
export default {
    name: 'AdminTransfersPage',
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

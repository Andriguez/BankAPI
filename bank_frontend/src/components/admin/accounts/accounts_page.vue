<script setup>
import UsersTable from '../users/users_table.vue'
import AccountsOverview from './accounts_overview.vue'
</script>

<template>
    <div class="d-flex">
        <UsersTable @selectUser="setUserId"/>
        <AccountsOverview v-if="userId !== 0" :id="userId"/>
    </div>
    <AccountsOverview/>
</template>

<script>
export default {
    name: 'AccountsPage',
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

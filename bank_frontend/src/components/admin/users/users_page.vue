<script setup>
import UsersTable from './users_table.vue'
import UsersOverview from './users_overview.vue'
</script>

<template>
    <div class="d-flex">
        <UsersTable @selectUser="setUserId"/>
        <UsersOverview v-if="userId !== 0" :id="userId"/>
    </div>
</template>

<script>
export default {
    name: 'Users',
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

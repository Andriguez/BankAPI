<script setup>
import UsersTable from './users_table.vue'
import UsersOverview from './users_overview.vue'
</script>

<template>
    <div class="d-flex">
        <UsersTable/>
        <UsersOverview/>
    </div>
</template>

<script>
export default {
    name: 'Users'
}
</script>

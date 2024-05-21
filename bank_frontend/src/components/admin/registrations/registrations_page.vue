<script setup>
import RegistrationsTable from './registrations_table.vue'
import RegistrationOverview from './registration_overview.vue'
</script>

<template>
    <div class="d-flex">
        <RegistrationsTable/>
        <RegistrationOverview/>
    </div>
</template>

<script>
export default {
    name: 'Registrations'
}
</script>

<script setup>
import RegistrationsTable from './registrations_table.vue'
import RegistrationOverview from './registration_overview.vue'
</script>

<template>
    <div class="d-flex">
        <RegistrationsTable @selectRegistration="setRegistrationId"/>
        <RegistrationOverview v-if="registrationId !== 0" :id="registrationId"/>
    </div>
</template>

<script>
export default {
    name: 'Registrations',
    data(){
        return{
            registrationId: 0
        }
    },
    methods: {
        setRegistrationId(id){
            this.registrationId = 0;

            this.$nextTick(() => {
                this.registrationId = id;
            });

        }
    }
}
</script>

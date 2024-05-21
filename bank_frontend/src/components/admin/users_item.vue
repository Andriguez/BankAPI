<template>

<li class="nav-item">
    <!--use class active to display the selected one-->
        <a href="#" class="nav-link text-white" aria-current="page" @click="selectRegistration(registration.Id)">
          {{ registration.firstName }} {{ registration.lastName }}
        </a>

    </li>

</template>

<script>
export default {
name: 'UsersItem',
props: {
    registration: Object
},
methods: {
    selectRegistration(id){
        this.$emit('selectRegistration', id);
    }
}
}
</script>

<style>
span{
    float: right;
}

.nav-item:hover{
 background-color: #6504c6;
}
</style>
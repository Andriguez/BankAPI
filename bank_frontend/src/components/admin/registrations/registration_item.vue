<template>

<li class="nav-item">
    <!--use class active to display the selected one-->
        <a href="#" class="nav-link text-white" aria-current="page">
          {{ registration.firstName }}
          <span>submitted 22/05/24</span>
        </a>

    </li>

</template>

<script>
export default {
name: 'RegistrationItem',
props: {
    registration: Object
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
<template>

<li class="nav-item">
    <!--use class active to display the selected one-->
        <a href="#" class="nav-link text-white" aria-current="page" @click="selectId(row.Id)">
          {{ row.firstName }} {{ row.lastName }}
        </a>

    </li>

</template>

<script>
export default {
name: 'UsersItem',
props: {
    row: Object
},
methods: {
    selectId(id){
        this.$emit('setId', id);
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
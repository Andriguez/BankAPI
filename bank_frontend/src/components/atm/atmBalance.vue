<template>
    <div class="container d-flex justify-content-center flex-wrap">
        <h1 class="m-3">User Full Name's Account(s)</h1>
        <div class="container account">
            <h3>Checking Account</h3>
            <h2>€1000</h2>
        </div>
        <div class="container account">
            <h3>Savings Account</h3>
            <h2>€2900</h2>
        </div>
    </div>
    <button class="back-btn m-3" @click="selectBtn('main')"><span>Go Back</span></button>


</template>
<script>
    export default{
        name: 'ATMbalance',
        emits: ['btn-selected'],
    methods: {
        selectBtn(){
            this.$emit('btn-selected', 'main');
        }
    }
    }
</script>

<style>
.account{
    border: solid 2px white;
    margin: 20px;
    text-align: center;
    width: 600px;
    padding: 10px;
}
</style>
<template>
     <div class="container d-flex justify-content-center m-3">
        <button class="atm-btn mx-4" @click="selectBtn('amount', 'WITHDRAWAL')"><h3>Withdraw</h3></button>
        <button class="atm-btn mx-4" @click="selectBtn('amount', 'DEPOSIT')"><h3>Deposit</h3></button>
        <button class="atm-btn mx-4" @click="selectBtn('balance', '')"><h3>Balance</h3></button>
     </div>
     <button class="exit-btn m-3" @click="$router.replace('/logout')"><h3>exit</h3></button>

</template>

<script>
export default {
    name: 'ATM',
    emits: ['btn-selected'],
    methods: {
        selectBtn(window, type){
            this.$emit('btn-selected', window, type);
        }
    }

}
</script>

<style>
.atm-btn{
    padding: 7px;
            margin: 5px;
            background-color: #201F1F;
            border: solid 3px #47008F;
            border-radius: 16px;
            height: 200px !important;
            width: 250px !important;

            font-size: 22px;
            color: #6504c6;
}

.exit-btn{
    padding: 7px;
            margin: 10px;
            background-color: #201F1F;
            border: solid 3px #E52E44;
            border-radius: 16px;
            height: 150px !important;
            width: 200px !important;
            float: right;

            font-size: 22px;
            color: #E52E44;
}

.atm-btn:hover{
    background-color: #8e6daf;
}

.exit-btn:hover {
    background-color: #d37680;
}
</style>
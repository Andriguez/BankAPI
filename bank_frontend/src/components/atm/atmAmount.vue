<template>
    <div class="container d-flex justify-content-center m-5">
       <button class="amount-btn"><h3>€10</h3></button>
       <button class="amount-btn"><h3>€20</h3></button>
       <button class="amount-btn"><h3>€50</h3></button>
       <button class="amount-btn"><h3>€100</h3></button>
       <input type="number" min="10" step="5" class="other-amount-btn" placeholder="Other Amount">


    </div>
    <button class="confirm-btn m-3"><span>Confirm</span></button>
    <button class="back-btn m-3" @click="selectBtn('main')"><span>Go Back</span></button>


</template>

<script>
export default {
   name: 'ATMamount',
   emits: ['btn-selected'],
   methods: {
        selectBtn(window){
            this.$emit('btn-selected', window);
        }
    }
}
</script>

<style>
.amount-btn{
   padding: 7px;
           margin: 5px;
           background-color: #201F1F;
           border: solid 3px #47008F;
           border-radius: 16px;
           height: 90px !important;
           width: 150px !important;

           font-size: 22px;
           color: #6504c6;
}

.other-amount-btn{
   padding: 7px;
           margin: 5px;
           background-color: #201F1F;
           border: solid 3px #47008F;
           border-radius: 16px;
           height: 90px !important;
           width: 250px !important;

           font-size: 32px;
           color: #6504c6;
}

.back-btn{
   padding: 7px;
           margin: 10px;
           background-color: #201F1F;
           border: solid 3px #E52E44;
           border-radius: 16px;
           height: 100px;
           width: 150px;
           float: right;

           font-size: 22px;
           color: #E52E44;
}

.confirm-btn{
   padding: 7px;
           margin: 10px;
           background-color: #201F1F;
           border: solid 3px #047616;
           border-radius: 16px;
           height: 100px;
           width: 150px;
           float: right;

           font-size: 22px;
           color: #047616;
}

.amount-btn:hover{
   background-color: #8e6daf;
}

.back-btn:hover {
   background-color: #d37680;
}

.confirm-btn:hover {
   background-color: #66ab70;
}
</style>
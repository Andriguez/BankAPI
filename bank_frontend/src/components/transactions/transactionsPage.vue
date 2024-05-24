<script setup>
import TransactionHistory from '@/components/transactions/transactionHistory.vue';
</script>

<template>
<div class="container mx-5 mt-5 mb-3" style="color: white;">
     <span style="font-size: 30px;">CURRENT</span>
     <br>
     <br>
     <span class="mx-5" style="font-size: 35px;">IBAN → NL11INHO0365135774</span>
     <span class="me-5" style="font-size: 35px; float: right;">€1000</span>
</div>

<div class="container mx-5">
    <button class="filter">filter</button>
    <button class="filter" >filter</button>
    <button class="filter">filter</button>

</div>

<div id="history-container" class="container d-flex mb-4 justify-content-center flex-wrap p-4">
    <TransactionHistory/>
</div>
</template>

<script>

</script>

<style>
.transaction{
    background-color: white;
    color: black;
    width: 40rem;
    font-size: 20px;
    border: solid 2px black;
}

.date{
    font-size: 25px;
}

.withdrawal{
    color: red;
}

.deposit{
    color: green;
}

#history-container{
    max-height: 20rem;
    width: 50rem;
    overflow-y: auto;
}

.filter{
    padding: 7px;
          margin: 5px;
          background-color: #201F1F;
          border: solid 3px #47008F;
          border-radius: 16px;
          height: 60px !important;
          width: 120px !important;

          font-size: 25px;
          color: #6504c6;}
</style>
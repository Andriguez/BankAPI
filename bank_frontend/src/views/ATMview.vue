<script setup>
import ATM from '../components/atm/atmMain.vue'
import ATMamount from '../components/atm/atmAmount.vue'
import ATMbalance from '../components/atm/atmBalance.vue'
</script>
<template>
         <img src="./../assets/atmLogo.png" alt="atm-logo" width="300" height="100%">
    <div v-if="selectedTab === 'main'">
        <ATM @btn-selected="changeWindow" />
    </div>
    <div v-else-if="selectedTab === 'amount'">
        <ATMamount @btn-selected="changeWindow" />
    </div>
    <div v-else-if="selectedTab === 'balance'">
        <ATMbalance @btn-selected="changeWindow" />
    </div>
</template>

<script>
export default {
name: 'ATMView',
components:{
    ATM,
    ATMamount,
    ATMbalance
},
data(){
        return{
            selectedTab: 'main',
            //selectedObject: null
        }
    },
    methods: {
        changeWindow(tab){
            this.selectedTab = '';
            //this.selectedObject = object;

            this.$nextTick(() => {
                this.selectedTab = tab;
            });
        }
    }
}
</script>
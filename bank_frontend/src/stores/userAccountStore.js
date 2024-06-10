import { defineStore } from "pinia";
import  axios  from "@/axios-auth"

export const useUserAccountStore = defineStore ('accountStore', {
    state: () => ({
        accounts: [],
        currentAccount: null,
        savingsAccount: null,
    }),
    getters: {
        userAccounts: (state) => state.accounts,
        userCurrentAccount: (state) => state.currentAccount,
        userSavingsAccount: (state) => state.savingsAccount,
    },
    actions: {
        async getUserAccounts() {
            
                try {
                    const response = await axios.get(`/accounts`);

                    this.accounts = Object.values(response.data.accounts);
                    this.currentAccount = this.accounts.find(account => account.type === 'CURRENT');
                    this.savingsAccount = this.accounts.find(account => account.type === 'SAVINGS');
                    //console.log(this.currentAccount);
                    //console.log(this.accounts);

                } catch (error) {
                    throw error.response.data;
                }
        
    } 
}

})
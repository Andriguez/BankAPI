import { defineStore } from "pinia";
import axios from "@/axios-auth"

export const useTransactionStore = defineStore('transactionStore', {
    state: () => ({
        accountInfo: null,
        transactionsList: [],
    }),
    getters: {
        getAccountInfo: (state) => state.accountInfo,
        getTransactionsList: (state) => state.transactionsList,
    },
    actions: {
        async getTransactionOfCustomerByType(accountType, transactionType = null, startDate = null, endDate = null, minAmount = null, exactAmount = null, maxAmount = null, iban = null, limit = null, skip = null) {
            try {
                let query = `accountType=${accountType}`;
                if (transactionType == "TRANSFER" || transactionType == "DEPOSIT" || transactionType == "WITHDRAWAL") {
                    query = `${query}&transactionType=${transactionType}`;
                }
                if (startDate != null) {
                    query = `${query}&startDate=${startDate}`;
                }
                if (endDate != null) {
                    query = `${query}&endDate=${endDate}`;
                }
                if (minAmount != null) {
                    query = `${query}&minAmount=${minAmount}`;
                }
                if (maxAmount != null) {
                    query = `${query}&maxAmount=${maxAmount}`;
                }
                if (exactAmount != null) {
                    query = `${query}&exactAmount=${exactAmount}`;
                }
                if (iban != null) {
                    query = `${query}&iban=${iban}`;
                }
                if (limit != null) {
                    query = `${query}&limit=${limit}`;
                }
                if (skip != null) {
                    query = `${query}&skip=${skip}`;
                }
                const response = await axios.get(`/transactions?${query}`);
                this.accountInfo = response.data.account;
                this.transactionsList = response.data.transactions;
                return response.data;
            } catch (error) {
                throw error.response.data;
            }
        },
        async createTransaction(transactionsData) {
            try {

                const data = {
                    sender: transactionsData.sender,
                    receiver: transactionsData.receiver,
                    amount: transactionsData.amount,
                    type: transactionsData.type
                }

                const response = await axios.post(`/transactions`, data);

                return response.data;

            } catch (error) {
                console.error(error.response.data);
                throw error.response.data;
            }
        },

        async filterTransactions(condition,userId = null, skip = null, limit = null) {
            try{
                let query = `condition=${condition}`;
                if(userId != null){
                    query = `${query}&userId=${userId}`;
                }
                if (limit != null) {
                    query = `${query}&limit=${limit}`;
                }
                if (skip != null) {
                    query = `${query}&skip=${skip}`;
                }
                const response = await axios.get(`/transactions/history?${query}`);
                this.transactionsList = response.data.transactions;
                return response.data
              } catch (error){
                console.log(error)
              }
        }
    }

})
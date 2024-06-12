import axios from '@/axios-auth.js';
export const getTransactionOfCustomerByType = async (accountType, transactionType=null, startDate=null, endDate=null, minAmount=null, exactAmount=null, maxAmount=null, iban=null) => {
    try {
        let query = `accountType=${accountType}`;
        if(transactionType == "TRANSFER" || transactionType == "DEPOSIT" || transactionType == "WITHDRAWAL") {
            query = `${query}&transactionType=${transactionType}`;
        }
        if(startDate != null) {
            query = `${query}&startDate=${startDate}`;
        }
        if(endDate != null) {
            query = `${query}&endDate=${endDate}`;
        }
        if(minAmount != null) {
            query = `${query}&minAmount=${minAmount}`;
        }
        if(maxAmount != null) {
            query = `${query}&maxAmount=${maxAmount}`;
        }
        if(exactAmount != null) {
            query = `${query}&exactAmount=${exactAmount}`;
        }
        if(iban != null) {
            query = `${query}&iban=${iban}`;
        }
        const response = await axios.get(`/transactions?${query}`);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const createTransaction = async (transactionsData) =>{
    try {

        const data = {
            sender: transactionsData.sender,
            receiver: transactionsData.receiver,
            amount: transactionsData.amount,
            type: transactionsData.type
        }

        const response = await axios.post(`/transactions`, data);
        
        return response.data;

    } catch (error){
        console.error(error.response.data);
        throw error.response.data;
    }
}

export const filterTransaction = async (userId,condition)=>{
    try{
        const response = await axios.get(`/transactions?id=${userId}&condition=${condition}`)
          return response.data
      } catch (error){
        console.log(error)
      }
}
import axios from '@/axios-auth.js';

export const getAccountsOfCustomer = async (token) => {
    try {
        console.log(token);
        const response = await axios.get(`/accounts/myAccounts`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const createAccounts = async (userId, currentData, savingsData) => {
    try {
        
        const data = {
            account1: currentData,
            account2: savingsData
        }

       const response = await axios.post(`accounts?userid=${userId}`, data)

       return response.data
       
    } catch (error) {
        console.error(error)
        throw error.response.data;
    }
}
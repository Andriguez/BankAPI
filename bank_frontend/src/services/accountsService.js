import axios from '@/axios-auth.js';

export const getAccountsOfCustomer = async (token) => {
    try {
        console.log(token);
        const response = await axios.get(`/accounts`, {
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
                absolute1: currentData.absolute,
                daily1: currentData.daily,
                type1: currentData.type,
                absolute2: savingsData.absolute,
                daily2: savingsData.daily,
                type2: savingsData.type
            }

       const response = await axios.post(`accounts?userid=${userId}`, data)

       return response.data
       
    } catch (error) {
        console.error(error)
        throw error.response.data;
    }
};

export const editAccountsInfo = async (userId, currentData, savingsData) => {
    try {
    
            const data = {
                absolute1: currentData.absolute,
                daily1: currentData.daily,
                type1: currentData.type,
                absolute2: savingsData.absolute,
                daily2: savingsData.daily,
                type2: savingsData.type
            }

       const response = await axios.put(`accounts?userid=${userId}`, data)

       return response.data
       
    } catch (error) {
        console.error(error)
        throw error.response.data;
    }
};
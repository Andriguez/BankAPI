import axios from '@/axios-auth.js';

export const getAccountsOfCustomer = async () => {
    try {
        const response = await axios.get(`/accounts`);
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

export const getAccountsById = async (userid) => {
    try {
        const response = await axios.get(`accounts?userid=${userid}`);
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
        throw error;
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
        throw error;
    }
};

export const closeAccounts = async (userId) => {
    try {
    
       const response = await axios.delete(`accounts?userid=${userId}`)

       return response.data
       
    } catch (error) {
        console.error(error)
        throw error;
    }
};
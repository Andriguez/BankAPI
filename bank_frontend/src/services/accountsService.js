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
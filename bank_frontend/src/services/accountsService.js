import axios from '@/axios-auth.js';

export const getAccountsByEmail = async (email, token) => {
    try {
        console.log(email, token);
        const response = await axios.get(`/accounts?email=${email}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};
import axios from '@/axios-auth.js';

export const getAccountsByUsername = async (username, token) => {
    try {
        console.log(username, token);
        const response = await axios.get(`/accounts?username=${username}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};
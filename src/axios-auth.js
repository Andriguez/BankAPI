import axios from "axios";

const instance = axios.create({baseURL: 'https://holladollabank.onrender.com/'});

export default instance;
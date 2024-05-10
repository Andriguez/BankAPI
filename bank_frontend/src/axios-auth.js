import axios from "axios";

const instance = axios.create({baseURL: 'https://localhost:8443/'});

export default instance;
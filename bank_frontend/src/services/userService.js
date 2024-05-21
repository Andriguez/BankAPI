import axios from '../axios-auth'

export const getRegistrations = (  ) => {
    return new Promise((resolve, reject) => {
      axios.get('/registrations', {
    })
    .then((res)=>{ 
        console.log(res.data);
        resolve(res.data)
    })
    .catch((error) => reject(error));
    },)}
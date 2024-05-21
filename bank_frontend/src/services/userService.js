import axios from '../axios-auth'

export const getRegistrations = (  ) => {
    return new Promise((resolve, reject) => {
      axios.get('/users/guest', {
    })
    .then((res)=>{ 
        console.log(res.data);
        resolve(res.data)
    })
    .catch((error) => reject(error));
    },)}
    
    export const getUsers = (  ) => {
      return new Promise((resolve, reject) => {
        axios.get('/users/customer', {
      })
      .then((res)=>{ 
          console.log(res.data);
          resolve(res.data)
      })
      .catch((error) => reject(error));
      },)}
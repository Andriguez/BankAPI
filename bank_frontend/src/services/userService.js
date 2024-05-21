import axios from '../axios-auth'

export const getRegistrations = (  ) => {
    return new Promise((resolve, reject) => {
      axios.get('/users/type/guest', {
    })
    .then((res)=>{ 
        console.log(res.data);
        resolve(res.data)
    })
    .catch((error) => reject(error));
    },)}

    export const getUserById = async (id) => {
      try{

      } catch (error){
        console.log(error)
      }
    }
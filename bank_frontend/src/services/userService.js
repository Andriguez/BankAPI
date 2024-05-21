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
        const response = await axios.get(`/users/id/${id}`)

          console.log(id)
          console.log(response.data)
      } catch (error){
        console.log(error)
      }
    }

    export const getUsers = (  ) => {
      return new Promise((resolve, reject) => {
        axios.get('/users/type/customer', {
      })
      .then((res)=>{ 
          console.log(res.data);
          resolve(res.data)
      })
      .catch((error) => reject(error));
      },)}
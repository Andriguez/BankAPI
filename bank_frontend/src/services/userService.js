import axios from '../axios-auth'

    export const getUsers = (type) => {
      return new Promise((resolve, reject) => {
        axios.get(`/users?type=${type}`, {
      })
      .then((res)=>{ 
          console.log(res.data);
          resolve(res.data)
      })
      .catch((error) => reject(error));
      },)}

      export const getUserById = async (id) => {
        try{
          const response = await axios.get(`/users?id=${id}`)
            console.log(response.data)
            console.log(id)
            return response.data
        } catch (error){
          console.log(error)
        }
      }

      export const requestRegistration = async (userData) => {
        return new Promise((resolve, reject) => {
            axios.post('/register', {
                firstName: userData.firstName,
                lastName: userData.lastName,
                bsnNumber: userData.bsnNumber,
                phoneNumber: userData.phoneNumber,
                email: userData.email,
                password: userData.password,
            })
            .then((res) => { 
                console.log(res.data);
                resolve(res.data);
            })
            .catch((error) => {
                console.error('Error:', error);
                reject(error);
            });
        });
    };
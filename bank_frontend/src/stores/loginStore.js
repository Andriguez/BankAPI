import { defineStore } from 'pinia'
import axios from '@/axios-auth';

export const useLoginStore = defineStore('login', {
  state: () => ({
    token: '',
    loggedIn: false,
    name: '',
    usertype: ''
  }),
  getters: {
    jwtToken: (state) => state.token,
    isLoggedIn: (state) => state.loggedIn,
    requestUserData: (state) => state.userData
  },
  actions: {
    requestLogin( email, password) {
      return new Promise((resolve, reject) => {
        axios.post('/login', {
          email: email,
          password: password,
      })
      .then((res)=>{ 
          axios.defaults.headers.common['Authorization'] = "Bearer " + res.data.token;
          this.token = res.data.token;
          this.loggedIn = true;
          this.name = res.data.name;
          this.usertype = res.data.usertype;
          localStorage.setItem('jwtToken', this.token);
          localStorage.setItem('name', JSON.stringify(res.data.name));
          localStorage.setItem('usertype', JSON.stringify(res.data.usertype))
          console.log(res.data);
          resolve()
      })
      .catch((error) => reject(error));
      },)
    },
    logout(){
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('name')
      localStorage.removeItem('usertype')

      this.loggedIn = false;

      delete axios.defaults.headers.common['Authorization'];
      return Promise.resolve('/');
    },
    retriveTokenFromStorage(){

      if(localStorage.getItem('jwtToken')){
        const localUserName= localStorage.getItem('name');
        const localUserType = localStorage.getItem('usertype')
        this.loggedIn = true;
        this.token = localStorage.getItem('jwtToken');
        this.name = JSON.parse(localUserName);
        this.usertype = JSON.parse(localUserType)
  
        axios.defaults.headers.common['Authorization'] = "Bearer " + this.token;
        return Promise.resolve('/');

      } else {
        return Promise.resolve('/login')
      }
    }
  },
})
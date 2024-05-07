import { defineStore } from 'pinia'
import axios from '@/axios-auth';

export const useLoginStore = defineStore('login', {
  state: () => ({
    token: '',
    loggedIn: false,
    userData: ''

  }),
  getters: {
    jwtToken: (state) => state.token,
    isLoggedIn: (state) => state.loggedIn,
    requestUserData: (state) => state.userData
  },
  actions: {
    requestLogin( username, password) {
      return new Promise((resolve, reject) => {
        axios.post('/users/login', {
          username: username,
          password: password,
      })
      .then((res)=>{ 
          axios.defaults.headers.common['Authorization'] = "Bearer " + res.data.jwt;
          this.token = res.data.jwt;
          this.loggedIn = true;
          this.userData = res.data;
          localStorage.setItem('jwtToken', res.data.jwt);
          localStorage.setItem('userData', JSON.stringify(res.data));
          console.log(res.data);
          resolve()
      })
      .catch((error) => reject(error));
      },)
    },
    logout(){
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('userData')
      this.loggedIn = false;

      delete axios.defaults.headers.common['Authorization'];
      return Promise.resolve('/');
    },
    retriveTokenFromStorage(){

      if(localStorage.getItem('jwtToken')){
        const localUserData = localStorage.getItem('userData');
        this.loggedIn = true;
        this.token = localStorage.getItem('jwtToken');
        this.userData = JSON.parse(localUserData);
        console.log(this.userData)
  
        axios.defaults.headers.common['Authorization'] = "Bearer " + this.token;
        return Promise.resolve('/');

      } else {
        return Promise.resolve('/login')
      }
    }
  },
})
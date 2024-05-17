import { defineStore } from 'pinia'
import axios from '@/axios-auth';

export const useLoginStore = defineStore('login', {
  state: () => ({
    token: '',
    loggedIn: false,
    username: ''
  }),
  getters: {
    jwtToken: (state) => state.token,
    isLoggedIn: (state) => state.loggedIn,
    requestUserData: (state) => state.userData
  },
  actions: {
    requestLogin( username, password) {
      return new Promise((resolve, reject) => {
        axios.post('/login', {
          username: username,
          password: password,
      })
      .then((res)=>{ 
          axios.defaults.headers.common['Authorization'] = "Bearer " + res.data.jwt;
          this.token = res.data.token;
          this.loggedIn = true;
          this.username = res.data.username;
          localStorage.setItem('jwtToken', res.data.token);
          localStorage.setItem('username', JSON.stringify(res.data.username));
          console.log(res.data);
          resolve()
      })
      .catch((error) => reject(error));
      },)
    },
    logout(){
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('username')
      this.loggedIn = false;

      delete axios.defaults.headers.common['Authorization'];
      return Promise.resolve('/');
    },
    retriveTokenFromStorage(){

      if(localStorage.getItem('jwtToken')){
        const localUserData = localStorage.getItem('username');
        this.loggedIn = true;
        this.token = localStorage.getItem('jwtToken');
        this.username = JSON.parse(localUserData);
        console.log(this.username)
  
        axios.defaults.headers.common['Authorization'] = "Bearer " + this.token;
        return Promise.resolve('/');

      } else {
        return Promise.resolve('/login')
      }
    }
  },
})
<script>
import { RouterLink, RouterView } from 'vue-router';
import Navigation from './components/navbar.vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

export default {
  name: 'App',
  components: {
    Navigation
  }
}
</script>

<template>
  <Navigation />
  <RouterView />
</template>

<style scoped>
</style>

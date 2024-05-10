<script>
import { RouterLink, RouterView } from 'vue-router';
import Navigation from './components/navbar.vue';

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

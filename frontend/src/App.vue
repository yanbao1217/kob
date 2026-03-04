<script setup>

import { ref, onMounted } from 'vue';

import axios from 'axios';
import NavBar from './components/NavBar.vue';


const api = axios.create({
  baseURL: 'http://127.0.0.1:8080', 
  timeout: 5000, 
  withCredentials: true 
});

</script>

<template>
  <NavBar />
  <RouterView />
</template>

<style scoped>

</style>

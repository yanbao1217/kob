<script setup>

import { ref, onMounted } from 'vue';

import axios from 'axios';
import NavBar from './components/NavBar.vue';


const api = axios.create({
  baseURL: 'http://127.0.0.1:8080', 
  timeout: 5000, 
  withCredentials: true 
});


const message = ref("")

onMounted(async () => {
  try {

    const response = await api.get('/pk/getBotInfo/');

    message.value = response.data;
    console.log("成功获取后端数据：", message.value); 
  } catch (error) {

    console.error("请求失败：", error.message);
    message.value = "请求失败，请检查后端服务！";
  }
});

</script>

<template>
  <NavBar />
  <RouterView />
</template>

<style scoped>

</style>

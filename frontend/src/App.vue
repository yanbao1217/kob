<script setup>
// 1. 导入 Vue 必要的响应式和生命周期函数
import { ref, onMounted } from 'vue';
// 2. 导入 axios（需先执行 npm install axios 安装）
import axios from 'axios';

// 4. 配置 axios 实例（统一处理请求地址和跨域）
const api = axios.create({
  baseURL: 'http://127.0.0.1:8080', // 后端服务地址
  timeout: 5000, // 请求超时时间
  withCredentials: true // 允许携带Cookie（配合后端跨域配置）
});


const message = ref("")
// 5. 页面挂载后调用后端接口
onMounted(async () => {
  try {
    // 调用后端 /pk/getBotInfo/ 接口
    const response = await api.get('/pk/getBotInfo/');
    // 将后端返回的 "hhhhh" 赋值给 message
    message.value = response.data;
    console.log("成功获取后端数据：", message.value); // 调试用
  } catch (error) {
    // 捕获错误并打印，方便排查
    console.error("请求失败：", error.message);
    message.value = "请求失败，请检查后端服务！";
  }
});

</script>

<template>
  <h1>{{ message }}</h1>
</template>

<style scoped>

</style>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<template>
    <div class="navbar bg-base-100 shadow-sm">
    <div class="navbar-start">
        <a class="btn btn-ghost text-xl">KOB</a>
    </div>
    <div class="navbar-center hidden lg:flex">
        <ul class="menu menu-horizontal px-1 active">
        <li><RouterLink :to="{name: 'pk_index'}" active-class="menu-focus">对战</RouterLink></li>
        <li><RouterLink :to="{name: 'record_index'}" active-class="menu-focus">对局列表</RouterLink></li>
        <li><RouterLink :to="{name: 'ranklist_index'}" active-class="menu-focus">排行榜</RouterLink></li>
        </ul>
    </div>
    <div class="navbar-end">
    <ul class="menu menu-horizontal right">
        <li class="relative"> <!-- 加相对定位，作为下拉菜单的参考 -->
        <details>
            <summary class="w-30 justify-center">ly</summary>
            <!-- 关键修改：加 align-right 让下拉菜单右对齐，z-10 确保层级 -->
            <ul class="p-2 bg-base-100 w-30 z-10 align-right absolute right-0">
            <li><RouterLink :to="{name: 'user_bot_index'}">我的bot</RouterLink></li>
            <li><a>退出</a></li>
            </ul>
        </details>
        </li>
    </ul>
    </div>
    </div>
</template>

<style scoped>

</style>
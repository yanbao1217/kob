<script setup>
import api from '@/js/http/api';
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import { useRouter } from 'vue-router';


const errorMessage = ref('');
const router = useRouter()
const user = useUserStore()
const username = ref('')
const password = ref('')

async function handleLogin() {
    try {
        const res = await api.post("/user/account/token/", {
            "username": username.value.trim(),
            "password": password.value.trim()
        })

        const data = res.data;
        if (data.error_message !== "success") {
            errorMessage.value = data.error_message;
        } else {
            user.setAccessToken(data.token)
            const new_user = {
                "id": data.user_id,
                "username": username.value,
                "photo": ""
            }
            user.setUserInfo(new_user)
            router.push({
                name: 'pk_index'
            })
        }
    } catch(err) {

    }
}

</script>

<template>
    <div class="flex justify-center mt-30">
        <form @submit.prevent="handleLogin" class="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">

            <label class="label">用户名</label>
            <input v-model="username" type="text" class="input" placeholder="请输入用户名" />

            <label class="label">密码</label>
            <input v-model="password" type="password" class="input" placeholder="请输入密码" />

            <p v-if="errorMessage" class="text-red-500 text-sm mt-1">{{errorMessage}}</p>

            <button class="btn btn-neutral mt-4">登录</button>
            <div class="flex justify-end">
                <RouterLink :to="{name: 'user_account_register'}" class="btn btn-sm btn-ghost text-gray-500">
                    注册
                </RouterLink> 
            </div>
        </form>
    </div>
</template>

<style scoped>

</style>
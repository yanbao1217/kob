<script setup>
import api from '@/js/http/api';
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const errorMessage = ref('');
const username = ref('');
const password = ref('');
const confirmedPassword = ref('');

const user = useUserStore();
const router = useRouter();

async function handleRegister() {
    try {
        const res = await api.post("user/account/register/", {
            username: username.value.trim(), 
            password: password.value.trim(),
            confirmedPassword: confirmedPassword.value.trim()
        })

        const data = res.data;
        if (data.error_message !== 'success') {
            errorMessage.value = data.error_message;
        } else {
            user.setUserInfo(data);
            await router.push({
                name: 'pk_index'
            })
        }
    } catch (err) {
 
    }

} 

</script>

<template>
    <div class="flex justify-center mt-30">
        <form @submit.prevent="handleRegister" class="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">

            <label class="label">用户名</label>
            <input v-model="username" type="text" class="input" placeholder="请输入用户名" />

            <label class="label">密码</label>
            <input v-model="password" type="password" class="input" placeholder="请输入密码" />
        
            <label class="label">确认密码</label>
            <input v-model="confirmedPassword" type="password" class="input" placeholder="请确认密码" />

            <p v-if="errorMessage" class="text-red-500 text-sm mt-1">{{ errorMessage }}</p>

            <button class="btn btn-neutral mt-4">注册</button>

            <div class="flex justify-end">
                <RouterLink :to="{name: 'user_account_login'}" class="btn btn-sm btn-ghost text-gray-500">
                    登录
                </RouterLink>
            </div>
        </form>
    </div>
</template>

<style scoped>

</style>
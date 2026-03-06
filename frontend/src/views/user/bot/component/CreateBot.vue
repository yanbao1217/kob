<script setup>
import api from '@/js/http/api';
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const description = ref('')
const title = ref('')
const content = ref('')
const error_message = ref('')

const user = useUserStore()

async function createBot() {
    try {   
        const res = await api.post("/user/rob/add/",
            {
                "title": title.value.trim(),
                "description": description.value.trim(),
                "content": content.value.trim()
            },
            {
                headers: {
                    "Authorization": `Bearer ${user.accessToken}`
                }
            }
        )

        const data = res.data;
        if (data.error_message !== "success") {
            error_message.value = data.error_message
        } else {
            error_message.value = "添加成功！"
        }
    } catch(err) {
        console.log(err)
    }
}

</script>

<template>
    <div class="flex justify-center mt-4">
        <fieldset class="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">

            <label class="label text-gray-500">名称</label>
            <input v-model="title" type="text" class="input" placeholder="名称" />

            <label class="label">描述</label>
            <textarea v-model="description" placeholder="请输入内容..." rows="5"></textarea>

            <label class="label">内容</label>
            <textarea v-model="content" placeholder="请输入内容..." rows="5"></textarea>

            <p v-if="error_message" class="text-red-500 text-sm">{{ error_message }}</p>

            <button @click="createBot" class="btn btn-neutral mt-4">添加</button>
        </fieldset>
    </div>
</template>

<style scoped>

</style>
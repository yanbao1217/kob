<script setup>
import api from '@/js/http/api';
import { useUserStore } from '@/stores/user';
import { ref, useTemplateRef } from 'vue';
import { useRoute } from 'vue-router';
import AddBotCode from './AddBotCode.vue';

const description = ref('')
const title = ref('')
const error_message = ref('')

const route = useRoute();
const botId = route.params.botId;
const user = useUserStore();

const codeRef = useTemplateRef('code-ref')

async function updateBot() {
    try {
        const content = codeRef.value.myCode
        const res = await api.post("/user/rob/update/", 
        {
            "rob_id": botId,
            "description": description.value.trim(),
            "title": title.value.trim(),
            "content": content.trim()
        },
        {
            headers: {
                "Authorization": `Bearer ${user.accessToken}`
            }
        })

        const data = res.data
        if (data.error_message !== "success") {
            error_message.value = data.error_message
        } else {
            error_message.value = "更新成功！"
        }
        
    } catch(err) {

    }
}

</script>

<template>
    <div class="flex justify-center mt-4">
        <fieldset class="fieldset bg-base-200 border-base-300 rounded-box w-xs border p-4">

            <label class="label text-gray-500">修改名称</label>
            <input v-model="title" type="text" class="input" placeholder="新名称" />

            <label class="label">修改描述</label>
            <textarea v-model="description" placeholder="请输入内容..." rows="5"></textarea>

            <label class="label">修改内容</label>
            <textarea placeholder="请输入内容..." rows="5"></textarea>

            <AddBotCode ref="code-ref" />

            <p v-if="error_message" class="text-red-500 text-sm">{{ error_message }}</p>

            <button @click="updateBot" class="btn btn-neutral mt-4">更新</button>
        </fieldset>
    </div>
</template>

<style scoped>

</style>
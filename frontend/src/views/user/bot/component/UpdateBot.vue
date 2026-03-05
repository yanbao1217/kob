<script setup>
import { ref } from 'vue';


const description = ref('')
const title = ref('')
const content = ref('')

await function updateBot() {
    
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
            <textarea v-model="content" placeholder="请输入内容..." rows="5"></textarea>

            <button class="btn btn-neutral mt-4">更新</button>
        </fieldset>
    </div>
</template>

<style scoped>

</style>
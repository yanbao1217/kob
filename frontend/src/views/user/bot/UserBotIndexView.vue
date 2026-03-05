<script setup>
import api from '@/js/http/api';
import { useUserStore } from '@/stores/user';
import { onMounted, ref } from 'vue';


const user = useUserStore()
const bots = ref([])
const error_message = ref('')

onMounted(() => {
    refresh_bots();
})

async function refresh_bots() {
    const res = await api.get("/user/rob/get_list/", {
        headers: {
            "Authorization": `Bearer ${user.accessToken}`
        }
    })
    const data = res.data

    bots.value = data;
}

async function delete_bot(botId) {
    try{
        const res = await api.post("/user/rob/remove/", 
        {
            "rob_id": botId
        },
        {   headers: {
                "Authorization": `Bearer ${user.accessToken}`
            }
        })

        const data = res.data
        if (data.error_message !== "success") {
            error_message.value = data.error_message
        } else {
            refresh_bots()
        }
    } catch(err) {
        console.log(err)
    }
}

</script>

<template>
    <div class="container flex justify-center">
        <div class="row">
            <div class="col-3">
                <div class="card mt-20">
                    <div class="card-body">
                        头像
                    </div>
                </div>
            </div>
        </div>
        <div class="col-9">
            <div class="card flex justify-center">
                <div class="card-header flex justify-center mt-4">
                    <span>我的bot</span>
                </div>
                <div class="card-body">
                    <div class="overflow-x-auto">
                        <table class="table table-zebra">
                            <!-- head -->
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <th></th>
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <RouterLink :to="{name: 'user_update_bot'}" class="btn text-gray-500 btn-sm btn-ghost">修改</RouterLink>
                                        <button @click="delete_bot(bot.id)" class="btn text-red-500 btn-sm btn-ghost">删除</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <button class="btn btn-sm text-gray-500 btn-ghost w-50">
                    创建bot
                </button>
            </div>
        </div>

        <p v-if="error_message" class="text-red-500 mt-4">{{ error_message }}</p>
    </div>
</template>

<style scoped>

</style>
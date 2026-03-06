<script setup>
import { pkStore } from '@/stores/pk';
import { useUserStore } from '@/stores/user';
import { ref } from 'vue';


const user = useUserStore();
const match_btn_info = ref("开始匹配")
const pk = pkStore();

async function click_match_btn() {
    if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消"
        pk.socket.send(JSON.stringify({
            event: "start-matching",
        }))
    } else {
        match_btn_info.value = "开始匹配"
    }
}

</script>

<template>
    <body class="bg-base-200 flex items-center justify-center h-screen">

    <div class="card w-[420px] h-[420px] bg-base-100 shadow-xl flex flex-col justify-between p-6">

        <!-- 对战区域 -->
        <div class="flex items-center justify-between flex-1">

        <!-- 左侧玩家 -->
        <div class="flex flex-col items-center gap-3">
            <div class="avatar">
                <div class="w-20 rounded-full">
                    <img src="https://api.dicebear.com/7.x/adventurer/svg?seed=player1"/>
                </div>
            </div>
            <span class="font-bold text-lg">{{ user.username }}</span>
        </div>

        <!-- VS -->
        <div class="flex flex-col items-center">
            <div class="w-16 h-16 rounded-full bg-primary text-primary-content flex items-center justify-center text-xl font-bold shadow-lg">
            VS
            </div>
        </div>

        <!-- 右侧玩家 -->
        <div class="flex flex-col items-center gap-3">
            <div class="avatar">
                <div class="w-20 rounded-full">
                    <img src="https://api.dicebear.com/7.x/adventurer/svg?seed=player2"/>
                </div>
            </div>
            <span class="font-bold text-lg">我的对手：{{ pk.opponent_username }}</span>
        </div>

        </div>

        <!-- 按钮 -->
        <div class="flex justify-center">
            <button @click="click_match_btn" class="btn btn-primary btn-wide text-lg">
                {{ match_btn_info }}
            </button>
        </div>

    </div>

    </body>
</template>

<style scoped>

</style>
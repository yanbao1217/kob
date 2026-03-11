<script setup>
import api from '@/js/http/api';
import { pkStore } from '@/stores/pk';
import { useUserStore } from '@/stores/user';
import { onMounted } from 'vue';
import { ref } from 'vue';


const user = useUserStore();
const match_btn_info = ref("开始匹配")
const pk = pkStore();
const bots = ref([])
const selectedOption = ref(-1)

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

async function click_match_btn() {
    if (match_btn_info.value === "开始匹配") {
        match_btn_info.value = "取消"
        pk.socket.send(JSON.stringify({
            event: "start-matching",
            bot_id: selectedOption.value,
        }))
    } else {
        match_btn_info.value = "开始匹配"
        pk.socket.send(JSON.stringify({
            event: "stop-matching",
        }))
    }
}

</script>

<template>
  <body class="bg-base-200 flex items-center justify-center h-screen">
    <div class="flex flex-col items-center gap-6">
      <!-- 下拉菜单 -->
      <div class="form-control w-72">
        <label class="label justify-center">
          <span class="label-text text-base font-semibold">请选择出场方式</span>
        </label>
        <select
          v-model="selectedOption"
          class="select select-bordered select-primary w-full text-center shadow-md"
        >
          <option value="-1" selected>亲自出场</option>
          <option v-for="bot in bots" :value="bot.id" :key="bot.id">{{ bot.title }}</option>
        </select>
      </div>

      <!-- 对战卡片 -->
      <div class="card w-[420px] h-[420px] bg-base-100 shadow-xl flex flex-col justify-between p-6">
        <!-- 对战区域 -->
        <div class="flex items-center justify-between flex-1">
          <!-- 左侧玩家 -->
          <div class="flex flex-col items-center gap-3">
            <div class="avatar">
              <div class="w-20 rounded-full">
                <img src="https://api.dicebear.com/7.x/adventurer/svg?seed=player1" />
              </div>
            </div>
            <span class="font-bold text-lg">{{ user.username }}</span>
          </div>

          <!-- VS -->
          <div class="flex flex-col items-center">
            <div
              class="w-16 h-16 rounded-full bg-primary text-primary-content flex items-center justify-center text-xl font-bold shadow-lg"
            >
              VS
            </div>
          </div>

          <!-- 右侧玩家 -->
          <div class="flex flex-col items-center gap-3">
            <div class="avatar">
              <div class="w-20 rounded-full">
                <img src="https://api.dicebear.com/7.x/adventurer/svg?seed=player2" />
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
    </div>
  </body>
</template>

<style scoped>

</style>
<script setup>
import { pkStore } from '@/stores/pk';
import { useUserStore } from '@/stores/user';
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';

const pk = pkStore()
const user = useUserStore();
const msg = ref("")
const router = useRouter()


watch(() => pk.loser, (newVal) => {
    console.log(pk.a_id)
    console.log(pk.b_id)
    console.log(user.id)

    if (newVal === "all") {
        msg.value = "Draw";
    } else if (newVal === "A" && String(pk.b_id) === user.id || newVal === "B" && String(pk.a_id) === user.id) {
        msg.value = "you win!";
    } else {
        msg.value = "you lose!";
    }
}, { immediate: true })

async function playAgain() {
    try {
        pk.updateLoser("none")
        pk.updateStatus("matching")
        await router.push({
            name: 'pk_index'
        })
    } catch(err) {
        console.log(err)
    }

}

</script>

<template>
  <!-- 弹窗遮罩层：半透明背景，点击可关闭 -->
  <div
    class="fixed inset-0 bg-black/30 flex items-center justify-center z-50"
  >
    <!-- 提示弹窗主体：居中显示，带圆角和动画 -->
    <div
      class="bg-white rounded-lg shadow-xl p-6 w-80 text-center transform transition-all duration-300"
    >
      <!-- 成功图标（可替换为自己的图标） -->
      <div class="text-4xl text-green-500 mb-3">{{ msg }}</div>
      <!-- 提示文本 -->
      <h3 class="text-lg font-medium text-gray-800">测试</h3>
      <!-- 关闭按钮（可选） -->
      <button @click="playAgain"
        class="mt-4 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition-colors cursor-pointer"
      >
        再来一局
      </button>
    </div>
  </div>
</template>

<style scoped>

</style>
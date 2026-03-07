import { defineStore } from "pinia";
import { ref } from "vue";

export const pkStore = defineStore('pk', () => {
    const status = ref("matching") // matching表示匹配，playing表示对战页面
    const socket = ref(null)
    const opponent_username = ref("")
    const opponent_photo = ref("")
    const gp = ref([])

    function updateSocket(newSocket) {
        socket.value = newSocket
    }

    function updateOpponent(opponent) {
        opponent_photo.value = opponent.opponent_photo
        opponent_username.value = opponent.opponent_username
    }

    function updateStatus(newStatus) {
        status.value = newStatus
    }

    function updateGameMap(data) {
        gp.value = data.gamemap
    }

    return {
        status,
        socket,
        opponent_photo,
        opponent_username,
        gp,
        updateOpponent,
        updateSocket,
        updateStatus,
        updateGameMap
    }
})
import { defineStore } from "pinia";
import { ref } from "vue";

export const pkStore = defineStore('pk', () => {
    const status = ref("matching") // matching表示匹配，playing表示对战页面
    const socket = ref(null)
    const opponent_username = ref("")
    const opponent_photo = ref("")
    const gp = ref([])
    const loser = ref("none") // all, A, B

    const a_id = ref(0);
    const a_sx = ref(0);
    const a_sy = ref(0);
    const b_id = ref(0);
    const b_sx = ref(0);
    const b_sy = ref(0);

    const gameObject = ref(null)

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

    function updateGame(data) {
        gp.value = data.map,
        a_id.value = data.a_id,
        a_sx.value = data.a_sx,
        a_sy.value = data.a_sy,
        b_id.value = data.b_id,
        b_sx.value = data.b_sx,
        b_sy.value = data.b_sy;
    }

    function updateGameObject(newGameObject) {
        gameObject.value = newGameObject;
    }

    function updateLoser(newLoser) {
        loser.value = newLoser;
    }

    return {
        status,
        socket,
        opponent_photo,
        opponent_username,
        gp,
        a_id,
        a_sx,
        a_sy,
        b_id, 
        b_sx,
        b_sy,
        gameObject,
        loser,
        updateOpponent,
        updateSocket,
        updateStatus,
        updateGame,
        updateGameObject,
        updateLoser
    }
})
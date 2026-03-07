<script setup>
import MatchGround from '@/components/MatchGround.vue';
import PlayGround from '@/components/PlayGround.vue';
import { pkStore } from '@/stores/pk';
import { useUserStore } from '@/stores/user';
import { nextTick, onMounted, onUnmounted } from 'vue';


const user = useUserStore();
const pk = pkStore();
const socketUrl = `ws://127.0.0.1:8080/websocket/${user.accessToken}`;

let socket = null;
onMounted(() => {
    try {

        socket = new WebSocket(socketUrl)

        socket.onopen = () => {
            console.log("connected!")
            pk.updateSocket(socket)
        }

        socket.onmessage = async msg => {
            const data = JSON.parse(msg.data)
            if (data.event === "start-matching") { // 匹配成功
                pk.updateOpponent(data)
                pk.updateGame(data.game)
                pk.gp = data.game.map

                await nextTick()

                setTimeout(() => {
                    pk.updateStatus("playing")
                }, 200)
                
            } else if (data.event === "move") {
                console.log(data)
                const game = pk.gameObject;
                console.log(game)
                const [snake0, snake1] = game.snakes;
                snake0.set_direction(data.a_direction);
                snake1.set_direction(data.b_direction);
            } else if (data.event === "result") {
                console.log(data)
            }
        }

        socket.onclose = () => {
            console.log("disconnected!")
            pk.updateStatus("matching")
        }
    } catch(err) {
        console.log(err)
    }
})

onUnmounted(() => {
    socket.close()
})

</script>

<template>
    <div class="flex justify-center">
        <PlayGround v-if="pk.status ==='playing'"/>
        <MatchGround v-if="pk.status === 'matching'"/>
    </div>
</template>

<style scoped>

</style>
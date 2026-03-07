<script setup>
import { GameMap } from '@/assets/scripts/GameMap';
import { pkStore } from '@/stores/pk';
import { onMounted, ref } from 'vue';

const parent = ref(null);
const canvas = ref(null);
const pk = pkStore();

onMounted(() => {
    new GameMap(canvas.value.getContext('2d'), parent.value, pk)
})

</script>

<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
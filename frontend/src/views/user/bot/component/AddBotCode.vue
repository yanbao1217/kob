<script setup>
import { VAceEditor } from 'vue3-ace-editor'
import 'ace-builds/src-noconflict/theme-chrome'
import 'ace-builds/src-noconflict/mode-java'
import { ref } from 'vue'

const myCode = ref('')

defineExpose({
  myCode,
})
</script>

<template>
  <label class="label">代码</label>
  <v-ace-editor
    v-model:value="myCode"
    style="height: 300px"
    lang="java"
    theme="chrome"
    rows="10"
    placeholder="请输入bot代码"
  />
</template>

<style scoped>
</style>
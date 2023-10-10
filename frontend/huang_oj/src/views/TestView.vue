<template>
  <div id="test">
    <MdEditor :value="inputValMd" :handle-change="onChangeMd" />
    <CodeEditor :currentCode="inputValCode" :handle-change="onChangeCode" />
  </div>
</template>
<script setup lang="ts">
import MdEditor from "@/components/MdEditor.vue";
import { ref } from "vue";
import CodeEditor from "@/components/CodeEditor.vue";

const inputValCode = ref("");

const onChangeCode = (v: string) => {
  inputValCode.value = v;
  console.log(inputValCode.value);
};

const inputValMd = ref("");

const onChangeMd = (v: string) => {
  inputValMd.value = v;
  console.log(inputValMd.value);
};
</script>

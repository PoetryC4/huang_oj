<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.arco-collapse-item-header {
  cursor: default;
}

.arco-collapse-item-header-left {
  cursor: default;
}
</style>
<script setup lang="ts">
import BasicLayout from "@/components/BasicLayout.vue";
import { onMounted } from "vue";

onMounted(() => {
  init();
});

const init = () => {
  console.log("init");
};
</script>

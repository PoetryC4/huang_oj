<template>
  <div id="basic_layout">
    <a-layout style="height: 400px">
      <a-layout-header class="header" v-if="route.meta?.basicLayout || false">
        <a-row class="grid-demo" style="margin-bottom: 16px">
          <a-col :flex="3">
            <GlobalHeader />
          </a-col>
          <a-col flex="1">
            <div>{{ store.state.user?.userInfo?.userName ?? "未登录" }}</div>
          </a-col>
        </a-row>
      </a-layout-header>
      <a-layout>
        <a-layout-sider
          theme="dark"
          class="sider"
          v-if="route.meta?.basicLayout || false"
          >Sider
        </a-layout-sider>
        <a-layout-content class="content">
          <router-view />
        </a-layout-content>
      </a-layout>
      <a-layout-footer class="footer" v-if="route.meta?.basicLayout || false"
        >Footer</a-layout-footer
      >
    </a-layout>
  </div>
</template>

<style scoped>
#basic_layout {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.sider {
  background: linear-gradient(135deg, #eeeeee, #ffffff);
  box-shadow: 2px 2px 4px 2px rgba(0, 0, 0, 0.2);
  height: 600px;
}

.content {
  background: white;
  height: 600px;
}

.header {
  background: linear-gradient(to right, #eeeeee, #ffffff);
  box-shadow: 2px 2px 4px 2px rgba(0, 0, 0, 0.2);
  margin-bottom: 12px;
}

.footer {
  background: linear-gradient(to right, #ffffff, #eeeeee);
  box-shadow: -2px -2px 4px 2px rgba(0, 0, 0, 0.2);
  position: absolute;
  right: 0;
  left: 0;
  top: auto;
  bottom: 0;
  height: 100px;
}
</style>
<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
import store from "@/store";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
</script>

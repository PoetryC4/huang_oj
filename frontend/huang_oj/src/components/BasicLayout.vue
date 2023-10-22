<template>
  <div id="basic_layout">
    <a-layout style="height: 400px">
      <a-layout-header class="header" v-if="route.meta?.basicLayout || false">
        <a-row class="grid-demo" style="margin-bottom: 16px">
          <a-col :flex="20">
            <GlobalHeader />
          </a-col>
          <a-col
            :flex="1"
            style="margin: auto 50px auto auto; cursor: pointer"
            @click="handleAvatarClick"
          >
            <a-avatar
              :style="{ backgroundColor: '#3370ff' }"
              v-if="store.state.user?.userInfo?.id === -1"
            >
              未登录
            </a-avatar>
            <a-avatar
              :style="{ backgroundColor: '#3370ff' }"
              v-else-if="
                store.state.user?.userInfo?.userAvatar === undefined ||
                store.state.user?.userInfo?.userAvatar === null
              "
            >
              {{ store.state.user?.userInfo?.userName || "用户" }}
            </a-avatar>
            <a-avatar v-else>
              <img
                alt="avatar"
                :src="
                  'http://127.0.0.1:8102/api/avatars/' +
                  store.state.user?.userInfo.id +
                  '/' +
                  store.state.user?.userInfo.userAvatar
                "
              />
            </a-avatar>
          </a-col>
        </a-row>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer" v-if="route.meta?.basicLayout || false">
        <a-typography style="margin-left: 22px">
          <a-typography-title :heading="5">项目开源地址:</a-typography-title>
          <a-typography-paragraph copyable>
            https://github.com/PoetryC4/huang_oj
          </a-typography-paragraph>
        </a-typography>
      </a-layout-footer>
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
  background: white;
  box-shadow: 2px 2px 4px 2px rgba(0, 0, 0, 0.2);
  height: 600px;
}

.content {
  background: white;
  height: 600px;
}

.header {
  background: white;
  box-shadow: 2px 2px 4px 2px rgba(0, 0, 0, 0.2);
  margin-bottom: 12px;
}

.footer {
  background: white;
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
import { onMounted } from "vue";

const router = useRouter();
const route = useRoute();

const handleAvatarClick = () => {
  router.push({
    path: "/user",
  });
};
</script>

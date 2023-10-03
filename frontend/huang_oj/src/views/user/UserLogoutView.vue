<template>
  <div id="user_logout_view">正在登出</div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";
import { UserControllerService } from "@/api";
import { useRouter } from "vue-router";
import store from "@/store";
import { Message } from "@arco-design/web-vue";

const router = useRouter();

onMounted(() => {
  handleLogout();
});

async function handleLogout() {
  let res;
  res = await UserControllerService.userLogoutUsingPost();
  if (res.code !== null && res.code === 1) {
    Message.success("登出成功");
    await store.dispatch("user/logoutUser");
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    Message.error(res.message || "出错");
  }
}
</script>

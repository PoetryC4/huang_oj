<template>
  <div id="global_header">
    <a-menu
      class="a_menu"
      mode="horizontal"
      :selected-keys="selectedKey"
      @menu-item-click="menuItemClicked"
    >
      <a-menu-item class="a_menu_item" key="0" disabled>
        <a-tag
          :color="'arcoblue'"
          bordered
          :style="{
            width: '68px',
            height: '35px',
            borderRadius: '10px',
            cursor: 'text',
            fontSize: '18px',
            color: '#0d3365',
            background: 'tranparent',
            marginRight: '50px',
          }"
        >
          Yi_OJ
        </a-tag>
      </a-menu-item>
      <a-menu-item
        v-for="item in visibleRoutes"
        :key="item.path"
        class="a_menu_item"
      >
        <a-tag style="font-size: 15px; margin-left: 10px; margin-right: 10px">
          {{ item.name }}
        </a-tag>
      </a-menu-item>
    </a-menu>
  </div>
</template>

<style scoped>
#global_header {
  margin-left: 10px;
  margin-right: auto;
  background: transparent;
}

.a_menu .a_menu_item {
  margin-right: auto;
  margin-left: 30px;
  background: transparent;
  padding: 0;
  float: left;
}

.a_menu {
  margin-right: auto;
  margin-left: 30px;
  background: transparent;
}
</style>
<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { computed, onMounted, ref } from "vue";
import { useStore } from "vuex";
import { roleChecker } from "@/components/scripts/access/roleCheck";
import { roleEnum } from "@/components/scripts/access/roleEnum";

const store = useStore();
const router = useRouter();
const route = useRoute();
const selectedKey = ref<string[]>(["/"]);
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    return (
      roleChecker(store.state?.user?.userInfo, item.meta?.access as string) &&
      item.meta?.hideInMenu !== true
    );
  });
});
/*router.afterEach((to, from, failure) => {
  //当前路由绑定
  selectedKey.value = [to.path];
});*/
const menuItemClicked = (key: string) => {
  selectedKey.value = [key];
  router.push({
    path: key,
  });
};
onMounted(() => {
  selectedKey.value = [route.path];
});
</script>

import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import AboutView from "@/views/AboutView.vue";
import AdminView from "@/views/AdminView.vue";
import NoAuthView from "@/views/NoAuthView.vue";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import UserView from "@/views/user/UserView.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: UserView,
    meta: {
      access: roleEnum.DEFAULT_USER,
    },
  },
  {
    path: "/user/login",
    name: "用户登录",
    component: UserLoginView,
    meta: {
      access: roleEnum.NOT_LOGIN,
      hideInMenu: true,
    },
  },

  {
    path: "/user/register",
    name: "用户注册",
    component: UserRegisterView,
    meta: {
      access: roleEnum.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: "/",
    name: "首页",
    component: HomeView,
    meta: {
      access: roleEnum.NOT_LOGIN,
    },
  },
  {
    path: "/about",
    name: "关于",
    component: AboutView,
    meta: {
      access: roleEnum.NOT_LOGIN,
    },
  },
  {
    path: "/admin",
    name: "管理员",
    component: AdminView,
    meta: {
      access: roleEnum.ADMIN,
    },
  },
  {
    path: "/noAuth",
    name: "无授权",
    component: NoAuthView,
    meta: {
      hideInMenu: true,
    },
  },
];

import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import AboutView from "@/views/AboutView.vue";
import AdminView from "@/views/AdminView.vue";
import NoAuthView from "@/views/NoAuthView.vue";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import UserView from "@/views/user/UserView.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserLogoutView from "@/views/user/UserLogoutView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import ProblemAddView from "@/views/problem/ProblemModifyView.vue";
import ProblemListView from "@/views/problem/ProblemListView.vue";
import TestView from "@/views/TestView.vue";
import ProblemDetailsView from "@/views/problem/ProblemDetailsView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/test",
    name: "test",
    component: TestView,
    meta: {
      access: roleEnum.ADMIN,
    },
  },
  {
    path: "/problem/list",
    name: "题目列表",
    component: ProblemListView,
    meta: {
      access: roleEnum.NOT_LOGIN,
    },
  },
  {
    path: "/problem/details/:id",
    name: "题目详情",
    component: ProblemDetailsView,
    props: true,
    meta: {
      access: roleEnum.NOT_LOGIN,
      hideInMenu: true,
    },
  },
  {
    path: "/problem/add",
    name: "添加题目",
    component: ProblemAddView,
    meta: {
      access: roleEnum.ADMIN,
    },
  },
  {
    path: "/user",
    name: "用户",
    component: UserView,
    meta: {
      access: roleEnum.DEFAULT_USER,
      basicLayout: true,
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
    path: "/user/logout",
    name: "用户登出",
    component: UserLogoutView,
    meta: {
      access: roleEnum.DEFAULT_USER,
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
      basicLayout: true,
    },
  },
  {
    path: "/about",
    name: "关于",
    component: AboutView,
    meta: {
      access: roleEnum.NOT_LOGIN,
      basicLayout: true,
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
      basicLayout: true,
    },
  },
];

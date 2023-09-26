import { StoreOptions } from "vuex";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { UserControllerService } from "@/api";
import store from "@/store";

export default {
  namespaced: true,
  state: () => ({
    userInfo: {
      userName: "未登录",
      userId: -1,
      loginDate: "",
      userRole: roleEnum.NOT_LOGIN,
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // userId === -1 代表未登录
      const res = await UserControllerService.getLoginUserUsingGet();
      if (res.code === 1) {
        commit("userLogin", res.data);
      } else {
        commit("userLogin", {
          ...state.userInfo,
          userRole: roleEnum.NOT_LOGIN,
        });
      }
    },
  },
  mutations: {
    userLogout(state) {
      state.userInfo.userName = "未登录";
      state.userInfo.userId = -1;
      state.userInfo.loginDate = "0000-00-00";
      state.userInfo.role = "none";
    },
    userLogin(state, payload) {
      state.userInfo = payload;
    },
  },
} as StoreOptions<any>;

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
      loginDate: new Date("1970-01-01T00:00:00"),
      userRole: roleEnum.NOT_LOGIN,
      isVip: 0,
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
          isVip: 0,
        });
      }
    },
    async logoutUser({ commit, state }) {
      commit("userLogout");
    },
  },
  mutations: {
    userLogout(state) {
      state.userInfo.userName = "未登录";
      state.userInfo.userId = -1;
      state.userInfo.loginDate = new Date("1970-01-01T00:00:00");
      state.userInfo.role = roleEnum.NOT_LOGIN;
      state.userInfo.isVip = 0;
    },
    userLogin(state, payload) {
      state.userInfo = payload;
    },
  },
} as StoreOptions<any>;

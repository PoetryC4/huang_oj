import { StoreOptions } from "vuex";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { UserControllerService } from "@/api";
import store from "@/store";

export default {
  namespaced: true,
  state: () => ({
    userInfo: {
      userAccount: null,
      userName: "未登录",
      id: -1,
      loginDate: new Date("1970-01-01T00:00:00"),
      userRole: roleEnum.NOT_LOGIN,
      isVip: 0,
      userAvatar: null,
      createTime: null,
      userProfile: null,
      userEmail: null,
    },
  }),
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // id === -1 代表未登录
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
      state.userInfo.id = -1;
      state.userInfo.loginDate = new Date("1970-01-01T00:00:00");
      state.userInfo.role = roleEnum.NOT_LOGIN;
      state.userInfo.isVip = 0;
      state.userInfo.createTime = null;
      state.userInfo.userAvatar = null;
      state.userInfo.userProfile = null;
      state.userInfo.userEmail = null;
      state.userInfo.userAccount = null;
    },
    userLogin(state, payload) {
      state.userInfo = payload;
    },
  },
} as StoreOptions<any>;

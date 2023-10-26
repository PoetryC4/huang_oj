<template>
  <div id="user_login_view">
    <a-tabs default-active-key="1" v-model:active-key="tabSelected">
      <a-tab-pane key="1" title="用户名登录">
        <a-form
          :model="state.formDataUserAccount"
          :style="{
            width: '600px',
            right: '0',
            left: '0',
            top: '0',
            bottom: '0',
            margin: '10% auto',
          }"
          @submit="handleLogin"
        >
          <a-form-item
            field="userAccount"
            tooltip="请输入用户名"
            label="用户名"
          >
            <a-input
              v-model="state.formDataUserAccount.userAccount"
              placeholder="请输入用户名..."
              feedback
              :rules="[{ required: true, message: '用户名是必需的' }]"
              :validate-trigger="['change', 'input']"
            />
          </a-form-item>
          <a-form-item field="userPassword" label="密码">
            <a-input-password
              v-model="state.formDataUserAccount.userPassword"
              placeholder="请输入密码..."
              feedback
              :rules="[{ required: true, message: '密码是必需的' }]"
              :validate-trigger="['change', 'input']"
              allow-clear
            />
          </a-form-item>
          <a-form-item>
            <a-button
              html-type="submit"
              style="inset: 0; margin: auto 25% auto auto; float: left"
              >登录
            </a-button>
            <a-button
              style="inset: 0; margin: auto auto auto 25%; float: right"
              @click="goToUpdatePassword"
              type="text"
              >修改密码
            </a-button>
            <a-button
              style="inset: 0; margin: auto auto auto auto; float: right"
              @click="goToRegister"
              type="text"
              >前往注册
            </a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="2" title="邮箱登录">
        <a-form
          :model="state.formDataUserEmail"
          :style="{
            width: '600px',
            right: '0',
            left: '0',
            top: '0',
            bottom: '0',
            margin: '10% auto',
          }"
          @submit="handleLogin"
        >
          <a-form-item
            field="userEmail"
            tooltip="请输入邮箱"
            label="邮箱"
            :rules="rules"
          >
            <a-input
              v-model="state.formDataUserEmail.userEmail"
              placeholder="请输入邮箱..."
              feedback
              :rules="[{ required: true, message: '邮箱是必需的' }]"
              :validate-trigger="['change', 'input']"
            />
          </a-form-item>
          <a-form-item field="userPassword" label="密码">
            <a-input-password
              v-model="state.formDataUserEmail.userPassword"
              placeholder="请输入密码..."
              feedback
              :rules="[{ required: true, message: '密码是必需的' }]"
              :validate-trigger="['change', 'input']"
              allow-clear
            />
          </a-form-item>
          <a-form-item>
            <a-button
              html-type="submit"
              style="inset: 0; margin: auto 25% auto auto; float: left"
              >登录
            </a-button>
            <a-button
              style="inset: 0; margin: auto auto auto 25%; float: right"
              @click="goToUpdatePassword"
              type="text"
              >修改密码
            </a-button>
            <a-button
              style="inset: 0; margin: auto auto auto auto; float: right"
              @click="goToRegister"
              type="text"
              >前往注册
            </a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService } from "@/api";
import { useRoute, useRouter } from "vue-router";
import store from "@/store";
import { Notification } from "@arco-design/web-vue";

const router = useRouter();
const route = useRoute();

const state = reactive({
  formDataUserAccount: {
    userAccount: "",
    userPassword: "",
  },
  formDataUserEmail: {
    userEmail: "",
    userPassword: "",
  },
});
const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

function isValidEmail(email: string): boolean {
  return emailRegex.test(email);
}

const rules = [
  {
    validator: (value: string, cb: any) => {
      return new Promise<void>((resolve) => {
        window.setTimeout(() => {
          if (!isValidEmail(value)) {
            cb("邮箱格式错误");
          }
          resolve();
        }, 2000);
      });
    },
  },
];
const tabSelected = ref("1");

const goToRegister = () => {
  router.push({
    path: "/user/register",
  });
};

const goToUpdatePassword = () => {
  router.push({
    path: "/user/password",
  });
};

async function handleLogin() {
  let res;
  if (tabSelected.value === "1") {
    res = await UserControllerService.userLoginUsingPost({
      ...state.formDataUserAccount,
      isEmail: false,
      userEmail: "",
    });
  } else {
    if (!isValidEmail(state.formDataUserEmail.userEmail)) {
      Notification.error({
        title: "警告",
        content: "邮箱格式不正确!",
        closable: true,
      });
      return;
    }
    res = await UserControllerService.userLoginUsingPost({
      ...state.formDataUserEmail,
      isEmail: true,
      userAccount: "",
    });
  }
  if (res.code !== null && res.code === 1) {
    console.log(res.data);
    console.log("登录成功");
    await store.dispatch("user/getLoginUser", {
      ...res.data,
      loginDate: new Date(),
    });
    let redirect = route.query.redirect;
    if (redirect == null) {
      redirect = "";
    }
    router.push({
      path: (redirect as string) || "/",
      replace: true,
    });
  } else {
    console.error(res.message || "出错");
  }
}
</script>

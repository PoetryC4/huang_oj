<template>
  <div id="user_update_password_view">
    <a-form
      :model="state.formDataPassword"
      :style="{
        width: '600px',
        right: '0',
        left: '0',
        top: '0',
        bottom: '0',
        margin: '10% auto',
      }"
      @submit="handleUpdatePassword"
    >
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="state.formDataPassword.userPassword"
          placeholder="请输入新密码..."
          feedback
          :rules="[
            { required: true, message: '密码是必需的' },
            { minLength: 8, message: '密码长度至少为8' },
          ]"
          :validate-trigger="['change', 'input']"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="checkPassword" label="确认密码">
        <a-input-password
          v-model="state.formDataPassword.checkPassword"
          placeholder="请再次输入新密码..."
          feedback
          :rules="[
            { required: true, message: '密码是必需的' },
            { minLength: 8, message: '密码长度至少为8' },
          ]"
          :validate-trigger="['change', 'input']"
          allow-clear
        />
      </a-form-item>
      <a-form-item
        field="userEmail"
        tooltip="请输入邮箱"
        label="邮箱"
        :rules="rules"
      >
        <a-input
          v-model="state.formDataPassword.userEmail"
          placeholder="请输入邮箱..."
          feedback
          :rules="[{ required: true, message: '邮箱是必需的' }]"
          :validate-trigger="['change', 'input']"
        />
      </a-form-item>
      <a-form-item field="emailVerifyCode" label="验证码">
        <a-input-number
          v-model="state.formDataPassword.emailVerifyCode"
          placeholder="请输入验证码"
          feedback
          :rules="[{ required: true, message: '验证码是必需的' }]"
          :validate-trigger="['change', 'input']"
          allow-clear
        />
        <a-button @click="sendVerifyCode" :disabled="codeSendDisable"
          >{{ codeSendTxt }}
        </a-button>
      </a-form-item>
      <a-form-item>
        <a-button
          html-type="submit"
          style="right: 0; left: 0; top: 0; bottom: 0; margin: auto"
          >修改密码
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService } from "@/api";
import { useRouter } from "vue-router";
import { Message, Notification } from "@arco-design/web-vue";
import { sleep } from "@/components/scripts/utils";
import store from "@/store";

const router = useRouter();

const codeSendTxt = ref("发送验证码");

const codeSendDisable = ref(false);

const state = reactive({
  formDataPassword: {
    userPassword: "",
    checkPassword: "",
    userEmail: "",
    emailVerifyCode: "",
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

async function sendVerifyCode() {
  if (!isValidEmail(state.formDataPassword.userEmail)) {
    Notification.error({
      title: "警告",
      content: "邮箱格式不正确!",
      closable: true,
    });
    return;
  }
  let res;
  res = await UserControllerService.sendVerifyCodeUsingGet(
    state.formDataPassword.userEmail
  );
  if (res.code !== null && res.code === 1) {
    Message.info("发送成功");
    codeSendDisable.value = true;
    for (let i = 300; i >= 0; i--) {
      codeSendTxt.value = "有效时间" + i + "秒";
      await sleep(1000);
    }
    codeSendDisable.value = false;
  } else {
    Message.error(res.message || "出错");
  }
}

async function handleUpdatePassword() {
  if (!isValidEmail(state.formDataPassword.userEmail)) {
    Notification.error({
      title: "警告",
      content: "邮箱格式不正确!",
      closable: true,
    });
    return;
  }
  let res;
  res = await UserControllerService.userUpdatePasswordUsingPost({
    ...state.formDataPassword,
  });
  if (res.code !== null && res.code === 1) {
    Message.success("密码修改成功");
    if (store.state.user?.userInfo.id > -1) {
      res = await UserControllerService.userLogoutUsingPost();
      if (res.code !== null && res.code === 1) {
        await store.dispatch("user/logoutUser");
        router.push({
          path: "/user/login",
          replace: true,
        });
      } else {
        Message.error(res.message || "出错");
      }
    }
    router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    Message.error(res.message || "出错");
  }
}
</script>

<template>
  <div id="user_profile_view">
    <a-form
      style="left: 0; right: 0; top: 0; bottom: 0; margin: 10% auto"
      :model="data"
      :style="{ width: '600px', marginTop: '20px' }"
      :size="'medium'"
      :auto-label-width="true"
    >
      <a-form-item field="id" label="用户id" feedback>
        <a-input
          readonly
          v-model="data.immutableData.id"
          placeholder="用户id"
        />
      </a-form-item>
      <a-form-item field="userAccount" label="用户账户" feedback>
        <a-input
          readonly
          v-model="data.immutableData.userAccount"
          placeholder="用户账户"
        />
      </a-form-item>
      <a-form-item
        field="userName"
        label="用户名称"
        feedback
        :rules="[
          { required: true, message: '用户名称是必需的' },
          { maxLength: 20, message: '用户名称不得长于二十个字符' },
        ]"
        :validate-trigger="['change', 'input']"
      >
        <a-input
          v-model="data.mutableData.userName"
          placeholder="请输入用户名称"
        />
      </a-form-item>
      <a-form-item field="createTime" label="加入日期" feedback>
        <a-input
          readonly
          v-model="data.immutableData.createTime"
          placeholder="加入日期"
        />
      </a-form-item>
      <a-form-item field="userEmail" label="用户邮箱" feedback>
        <a-input
          readonly
          v-model="data.immutableData.userEmail"
          placeholder="用户邮箱"
        />
      </a-form-item>
      <a-form-item
        field="userProfile"
        label="用户简介"
        feedback
        :rules="[{ maxLength: 200, message: '用户名称不得长于200个字符' }]"
        :validate-trigger="['change', 'input']"
      >
        <a-input
          v-model="data.mutableData.userProfile"
          placeholder="请输入用户简介"
        />
      </a-form-item>
      <!--      <a-upload
              style="display: flex; justify-content: center; align-items: center"
              draggable
              :limit="1"
              action="#"
              :headers="headers"
              :auto-upload="false"
              :fileList="avatar ? [avatar] : []"
              :show-file-list="false"
              @change="onChange"
              @progress="onProgress"
              :on-exceed-limit="handleExceed"
              :on-before-upload="beforeUpload"
              accept=".jpg,.jpeg,.png,.JPG,.JPEG"
            >
              <template #upload-button>
                <div
                  :class="`arco-upload-list-item${
                    avatar && avatar.status === 'error'
                      ? ' arco-upload-list-item-error'
                      : ''
                  }`"
                >
                  <div
                    class="arco-upload-list-picture custom-upload-avatar"
                    v-if="avatar && avatar.url"
                  >
                    <img :src="avatar.url" />
                    <div class="arco-upload-list-picture-mask">
                      <IconEdit />
                    </div>
                    <a-progress
                      v-if="avatar.status === 'uploading' && avatar.percent < 100"
                      :percent="avatar.percent"
                      type="circle"
                      size="mini"
                      :style="{
                        position: 'absolute',
                        left: '50%',
                        top: '50%',
                        transform: 'translateX(-50%) translateY(-50%)',
                      }"
                    />
                  </div>
                  <div class="arco-upload-picture-card" v-else>
                    <div class="arco-upload-picture-card-text">
                      <IconPlus />
                      <div style="margin-top: 10px; font-weight: 600">Upload</div>
                    </div>
                  </div>
                </div>
              </template>
            </a-upload>-->
      <a-form-item
        style="display: flex; justify-content: center; align-items: center"
      >
        <a-space
          style="
            display: flex;
            align-items: center;
            justify-content: center;
            inset: 0;
            margin: 7% auto auto 31%;
          "
        >
          <a-button long type="primary" @click="handleUserInfoUpdate"
            >保存用户信息
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import store from "@/store";
import { useRoute, useRouter } from "vue-router";
import { Message } from "@arco-design/web-vue";
import { UserControllerService } from "@/api";
import moment from "moment/moment";

const router = useRouter();
const route = useRoute();

/*const headers = {
  "Content-Type": "multipart/form-data",
};*/

const curUser = ref({});
/*const avatar = ref();*/

const data = reactive({
  mutableData: {
    userName: "",
    /*userAvatar: "",*/
    userProfile: "",
  },
  immutableData: {
    id: "",
    createTime: "",
    userEmail: "",
    userAccount: "",
  },
});
/*const onChange = (_: any, currentFile: any) => {
  avatar.value = {
    ...currentFile,
    // url: URL.createObjectURL(currentFile.file),
  };
};
const onProgress = (currentFile: any) => {
  avatar.value = currentFile;
};
const handleExceed = (files: any, fileList: any) => {
  Message.warning(
    `当前限制选择 1 个图片，本次选择了 ${files.length} 个文件，共选择了 ${
      files.length + fileList.length
    } 个文件`
  );
};
const beforeUpload = (file: any) => {
  const maxSize = 1024 * 1024; // 1 MB (您可以根据需要修改文件大小限制)

  if (file.size > maxSize) {
    Message.warning("文件大小超过限制（最大允许 1MB）");
    return false; // 阻止文件上传
  }
  return true; // 允许文件上传
};*/
const handleUserInfoUpdate = async () => {
  let res1 = await UserControllerService.updateMyUserUsingPost({
    userName: data.mutableData.userName,
    /*userAvatar: data.mutableData.userAvatar,*/
    userProfile: data.mutableData.userProfile,
  });
  if (res1.code === 0) {
    Message.error("err" + res1.message);
    return;
  }
  /*if (avatar.value != null) {
    const _file = avatar.value.file;
    let blob = new Blob([_file], { type: "image/png" });

    const formData = new FormData();
    formData.append("file", _file);

    let res2 = await UserControllerService.updateAvatarUsingPost(formData);
    if (res2.code === 0) {
      Message.error("err" + res2.message);
      return;
    }
  }*/
  Message.success("修改成功");
  await store.dispatch("user/getLoginUser");
  getUserThis();
};
const getUserThis = () => {
  curUser.value = store.state.user?.userInfo || {};
  if (curUser.value !== {}) {
    data.mutableData.userName = curUser.value?.userName;
    /*data.mutableData.userAvatar = curUser.value?.userAvatar;*/
    data.mutableData.userProfile = curUser.value?.userProfile;
    data.immutableData.id = curUser.value?.id;
    data.immutableData.createTime = moment(
      curUser.value?.createTime || new Date()
    ).format("YYYY-MM-DD");
    data.immutableData.userEmail = curUser.value?.userEmail;
    data.immutableData.userAccount = curUser.value?.userAccount;
  } else {
    Message.error("获取用户出错");
  }
};
onMounted(async () => {
  getUserThis();
});
</script>

<style scoped>
.half-divider {
  min-width: 100%;
  margin: 16px 0;
}
</style>

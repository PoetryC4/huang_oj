<template>
  <div id="user_view">
    <a-row class="grid-demo" :gutter="24">
      <a-col :span="6" :offset="5">
        <a-card
          :style="{
            width: '95%',
            marginBottom: '20px',
            marginTop: '20px',
            marginLeft: '30px',
          }"
          hoverable
        >
          <div @click="handleAvatarClick">
            <a-avatar
              :size="60"
              :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
              v-if="curUser.id === -1"
            >
              未登录
            </a-avatar>
            <a-avatar
              :size="60"
              :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
              v-else-if="
                curUser.userAvatar === undefined || curUser.userAvatar === null
              "
            >
              {{ curUser.userName || "用户" }}
            </a-avatar>
            <a-avatar v-else :size="60" :style="{ marginLeft: '20px' }">
              <img
                alt="avatar"
                :src="
                  'http://127.0.0.1:8102/api/user/avatars/' +
                  curUser.id +
                  '/' +
                  curUser.userAvatar
                "
              />
            </a-avatar>
          </div>
          <a-typography-text style="font-size: 17px; margin-left: 20px">
            {{ curUser.userName || "用户" }}
          </a-typography-text>
          <a-button
            type="dashed"
            @click="goToInfoChange"
            style="margin-right: 20px; margin-left: auto; float: right"
            >修改信息
          </a-button>
          <a-button
            type="dashed"
            @click="handleUserLogout"
            style="margin-right: 20px; margin-left: auto; float: right"
            >退出登录
          </a-button>
          <a-divider class="half-divider" />
          <a-typography-text style="font-size: 17px; margin-left: 20px">
            {{
              moment(
                store.state.user?.userInfo?.createTime || new Date()
              ).format("YYYY-MM-DD")
            }}
          </a-typography-text>
          <a-divider class="half-divider" />
          <a-typography-text style="font-size: 17px; margin-left: 20px">
            简介:{{ store.state.user?.userInfo?.userProfile || "" }}
          </a-typography-text>
          <a-divider class="half-divider" />
          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: left;
              color: #2a2a2a;
            "
          >
            简单题:
            <span
              style="
                font-size: 24px;
                font-weight: bold;
                margin-right: 6px;
                margin-left: 8px;
                color: green;
              "
              >{{ userRecord.easySolved || 0 }}</span
            >
            / <span style="color: #2a2a2a">{{ userRecord.easyTotal }}</span>
          </a-typography-text>
          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: right;
              color: #2a2a2a;
              font-weight: bold;
            "
          >
            {{
              (
                ((userRecord.easySolved || 0) /
                  Math.max(userRecord.easyTried || 1, 1)) *
                100
              ).toFixed(1)
            }}%
          </a-typography-text>
          <a-divider class="half-divider" />

          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: left;
              color: #2a2a2a;
            "
          >
            中等题:
            <span
              style="
                font-size: 24px;
                font-weight: bold;
                margin-right: 6px;
                margin-left: 8px;
                color: orange;
              "
              >{{ userRecord.mediumSolved || 0 }}</span
            >
            / <span style="color: #2a2a2a">{{ userRecord.mediumTotal }}</span>
          </a-typography-text>
          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: right;
              color: #2a2a2a;
              font-weight: bold;
            "
          >
            {{
              (
                ((userRecord.mediumSolved || 0) /
                  Math.max(userRecord.mediumTried || 1, 1)) *
                100
              ).toFixed(1)
            }}%
          </a-typography-text>
          <a-divider class="half-divider" />

          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: left;
              color: #2a2a2a;
            "
          >
            困难题:
            <span
              style="
                font-size: 24px;
                font-weight: bold;
                margin-right: 6px;
                margin-left: 8px;
                color: firebrick;
              "
              >{{ userRecord.hardSolved || 0 }}</span
            >
            / <span style="color: #2a2a2a">{{ userRecord.hardTotal }}</span>
          </a-typography-text>
          <a-typography-text
            style="
              font-size: 17px;
              margin-left: 20px;
              float: right;
              color: #2a2a2a;
              font-weight: bold;
            "
          >
            {{
              (
                ((userRecord.hardSolved || 0) /
                  Math.max(userRecord.hardTried || 1, 1)) *
                100
              ).toFixed(1)
            }}%
          </a-typography-text>
          <a-divider class="half-divider" />
        </a-card>
        <a-modal
          v-model:visible="modalVisible"
          @cancel="modalVisible = false"
          :on-before-ok="handleModalBeforeOk"
          unmountOnClose
        >
          <template #title> 更改头像</template>
          <a-upload
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
          </a-upload>
        </a-modal>
      </a-col>
      <a-col :span="8">
        <a-card
          :style="{
            width: '95%',
            marginBottom: '20px',
            marginTop: '20px',
            marginLeft: '30px',
          }"
          hoverable
        >
          <a-tabs @change="handleUserTabChange">
            <a-tab-pane :key="1">
              <template #title>
                <icon-select-all />
                提交记录
              </template>
              <a-row style="margin-top: 30px; margin-bottom: 30px">
                <a-col :span="7" :offset="1">
                  <a-input
                    placeholder="请输入要搜索的内容"
                    search-button
                    style="width: 99%"
                    v-model="titleInput"
                    @press-enter="getMySubmissionList"
                  >
                    <template #button-icon>
                      <icon-search />
                    </template>
                  </a-input>
                </a-col>
                <a-col :span="5" :offset="1">
                  <a-select
                    v-model="selectedStatus"
                    placeholder="状态选择"
                    @change="getMySubmissionList"
                  >
                    <a-option
                      v-for="submissionResult in SubmissionResultEnums"
                      :value="submissionResult.value"
                      :key="submissionResult.value"
                    >
                      {{ submissionResult.label }}
                    </a-option>
                  </a-select>
                </a-col>
              </a-row>
              <a-table
                :data="data.mySubmissionTable"
                stripe
                :pagination="false"
              >
                <template #columns>
                  <a-table-column title="题目名称" :width="150" align="center">
                    <template #cell="{ record }">
                      <a-button
                        type="text"
                        long
                        @click="handleGoToProblem(record.problemId)"
                        style="left: 5px"
                        >{{ record.problemId }} {{ record.title }}
                      </a-button>
                    </template>
                  </a-table-column>
                  <a-table-column title="结果" :width="120" align="center">
                    <template #cell="{ record }">
                      <a-button
                        type="text"
                        long
                        @click="handleGoToProblem(record.problemId)"
                        style="left: 5px"
                      >
                        <div
                          v-if="
                            record.judgeResult.judgeInfo?.resultStr ===
                            'Accepted'
                          "
                          style="color: green; font-size: 18px"
                        >
                          {{ record.judgeResult.judgeInfo?.resultStr || "" }}
                        </div>
                        <div v-else style="color: red; font-size: 18px">
                          {{ record.judgeResult.judgeInfo?.resultStr || "" }}
                        </div>
                      </a-button>
                    </template>
                  </a-table-column>
                  <a-table-column title="提交日期" :width="90">
                    <template #cell="{ record }">
                      {{ moment(record?.submitTime).format("YYYY-MM-DD") }}
                    </template>
                  </a-table-column>
                </template>
              </a-table>
              <a-pagination
                :total="data.mySubmissionCount"
                show-total
                show-jumper
                show-page-size
                :page-size-options="subPageSizes"
                @page-size-change="handleSubSizeChange"
                @change="handleSubCurrentChange"
                v-model:current="subCurPage"
                v-model:page-size="subPageSize"
                style="
                  left: 0;
                  right: 0;
                  top: 0;
                  bottom: 0;
                  margin: 2% auto;
                  padding: 0;
                  justify-content: center;
                  -webkit-justify-content: center;
                "
              >
                <template #page-item="{ page }"> - {{ page }} -</template>
                <template #page-item-step="{ type }">
                  <icon-send
                    :style="
                      type === 'previous'
                        ? { transform: `rotate(180deg)` }
                        : undefined
                    "
                  />
                </template>
                <template #page-item-ellipsis>
                  <icon-sun-fill />
                </template>
              </a-pagination>
            </a-tab-pane>
            <a-tab-pane :key="2">
              <template #title>
                <icon-message />
                评论记录
              </template>

              <a-row
                style="
                  display: flex;
                  justify-content: space-between;
                  flex-wrap: wrap;
                  margin-top: 30px;
                  margin-bottom: 30px;
                "
              >
                <a-col :span="9" :offset="1">
                  <a-input
                    placeholder="请输入要搜索的评论内容"
                    search-button
                    style="width: 99%"
                    v-model="commentSearchText"
                    @press-enter="getMyCommentList"
                  >
                    <template #button-icon>
                      <icon-search />
                    </template>
                  </a-input>
                </a-col>
              </a-row>
              <a-comment
                v-for="(item, index) of data.myCommentTable"
                :key="index"
                :datetime="
                  moment(
                    moment(item.createTime).format('YYYY-MM-DD HH:mm:ss'),
                    'YYYY-MM-DD HH:mm:ss'
                  ).fromNow()
                "
                :author="item.userVO.userName || '用户'"
              >
                <template #content>
                  <div v-if="curCommentEdit !== index">{{ item.content }}</div>
                  <a-textarea v-else v-model="item.content" />
                </template>
                <template #actions>
                  <a-tag
                    @click="handleGoToProblem(item.problemVO.id)"
                    style="cursor: pointer"
                    >{{ item.problemVO?.id || -1 }}
                    {{ item.problemVO?.title || "" }}
                  </a-tag>
                  <div
                    v-if="
                      curUser != undefined &&
                      curUser?.userRole !== undefined &&
                      curUser?.userRole !== null &&
                      curUser?.userRole === roleEnum.ADMIN
                    "
                  >
                    <a-button
                      size="small"
                      v-if="curCommentEdit !== index"
                      style="
                        margin-right: 10px;
                        margin-left: auto;
                        float: right;
                      "
                      status="danger"
                      type="primary"
                      @click="handleDeleteComment(item.id)"
                      >删除该评论
                    </a-button>
                    <a-button
                      size="small"
                      v-if="curCommentEdit !== index"
                      style="
                        margin-right: 10px;
                        margin-left: auto;
                        float: right;
                      "
                      status="normal"
                      type="primary"
                      @click="curCommentEdit = index"
                      >编辑该评论
                    </a-button>
                    <a-button
                      size="small"
                      v-if="curCommentEdit === index"
                      style="
                        margin-right: 10px;
                        margin-left: auto;
                        float: right;
                      "
                      status="normal"
                      type="primary"
                      @click="handleEditSubmit(item)"
                      >保存编辑
                    </a-button>
                  </div>
                </template>
                <template #avatar>
                  <a-avatar
                    :size="60"
                    :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
                    v-if="item.userVO.id === -1"
                  >
                    未登录
                  </a-avatar>
                  <a-avatar
                    :size="60"
                    :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
                    v-else-if="
                      item.userVO.userAvatar === undefined ||
                      item.userVO.userAvatar === null
                    "
                  >
                    {{ item.userVO.userName || "用户" }}
                  </a-avatar>
                  <a-avatar v-else :size="60" :style="{ marginLeft: '20px' }">
                    <img
                      alt="avatar"
                      :src="
                        'http://127.0.0.1:8102/api/user/avatars/' +
                        item.userVO.id +
                        '/' +
                        item.userVO.userAvatar
                      "
                    />
                  </a-avatar>
                </template>
                <a-divider />
              </a-comment>
              <a-pagination
                :total="data.myCommentCount"
                show-total
                show-jumper
                show-page-size
                :page-size-options="commentPageSizes"
                @page-size-change="handleCommentSizeChange"
                @change="handleCommentCurrentChange"
                v-model:current="commentCurPage"
                v-model:page-size="commentPageSize"
                style="
                  left: 0;
                  right: 0;
                  top: 0;
                  bottom: 0;
                  margin: 2% auto;
                  padding: 0;
                  justify-content: center;
                  -webkit-justify-content: center;
                "
              >
                <template #page-item="{ page }"> - {{ page }} -</template>
                <template #page-item-step="{ type }">
                  <icon-send
                    :style="
                      type === 'previous'
                        ? { transform: `rotate(180deg)` }
                        : undefined
                    "
                  />
                </template>
                <template #page-item-ellipsis>
                  <icon-sun-fill />
                </template>
              </a-pagination>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import store from "@/store";
import moment from "moment";
import { onMounted, reactive, ref } from "vue";
import {
  CommentControllerService,
  SubmissionControllerService,
  UserControllerService,
} from "@/api";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import {
  IconMessage,
  IconSearch,
  IconSelectAll,
  IconSend,
  IconSunFill,
  IconEdit,
  IconPlus,
} from "@arco-design/web-vue/es/icon";
import { SubmissionResultEnums } from "@/components/scripts/enum/SubmissionResultEnums";
import { roleEnum } from "@/components/scripts/access/roleEnum";

const userRecord = ref({});
const router = useRouter();

const curUser = store.state.user?.userInfo;

const titleInput = ref("");
const subCurPage = ref(1);
const subPageSize = ref(8);
const subPageSizes = ref([8, 16, 24]);
const commentCurPage = ref(1);
const commentPageSize = ref(8);
const commentPageSizes = ref([8, 16, 24]);
const selectedStatus = ref(null);
const commentSearchText = ref("");
const data = reactive({
  mySubmissionTable: [],
  mySubmissionCount: 0,
  myCommentTable: [],
  myCommentCount: 0,
});
const userAvatar = ref("");

const handleEditSubmit = async (item: any) => {
  let res = await CommentControllerService.editCommentUsingPost({
    id: item.id || -1,
    content: item.content || "",
    problemId: item.problemId || -1,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  curCommentEdit.value = -1;
};
const handleDeleteComment = async (id: number) => {
  let res = await CommentControllerService.deleteCommentUsingPost({
    id: id,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  } else {
    Message.success("删除成功");
    getMyCommentList();
  }
};
const getMySubmissionList = async () => {
  let res = await SubmissionControllerService.listMySubmissionVoByPageUsingPost(
    {
      current: subCurPage.value,
      pageSize: subPageSize.value,
      judgeStatus:
        selectedStatus.value === null || curUser.id < 0
          ? null
          : selectedStatus.value,
      title: titleInput.value === "" ? null : titleInput.value,
    }
  );
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  data.mySubmissionTable = res.data.records || [];
  data.mySubmissionCount = parseInt(res.data.total);
  if (
    Math.ceil(data.mySubmissionCount / subPageSize.value) < subCurPage.value
  ) {
    subCurPage.value = Math.ceil(data.mySubmissionCount / subPageSize.value);
  }
};
const curCommentEdit = ref(-1);
const getMyCommentList = async () => {
  /*let res = await CommentControllerService.listMyCommentVoByPageUsingPost({
    current: commentCurPage.value,
    pageSize: commentPageSize.value,
    searchText: commentSearchText.value,
  });*/
  let res = await CommentControllerService.searchCommentVoByPageUsingPost({
    current: commentCurPage.value,
    pageSize: commentPageSize.value,
    searchText: commentSearchText.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  curCommentEdit.value = -1;
  data.myCommentTable = res.data.records || [];
  data.myCommentCount = parseInt(res.data.total);
  if (
    Math.ceil(data.myCommentCount / commentPageSize.value) <
    commentCurPage.value
  ) {
    subCurPage.value = Math.ceil(data.myCommentCount / commentPageSize.value);
  }
};
const handleSubSizeChange = (val: number) => {
  subPageSize.value = val;
  getMySubmissionList();
};
const handleSubCurrentChange = (val: number) => {
  subCurPage.value = val;
  getMySubmissionList();
};
const handleCommentSizeChange = (val: number) => {
  subPageSize.value = val;
  getMyCommentList();
};
const handleCommentCurrentChange = (val: number) => {
  subCurPage.value = val;
  getMyCommentList();
};
const goToInfoChange = () => {
  if (store.state.user?.userInfo?.id < 0) {
    return;
  }
  router.push({
    path: "/user/profile",
  });
};
const handleGoToProblem = (id: number) => {
  if (id == null || id < 0) {
    return;
  }
  router.push({
    path: "/problem/details/" + id,
  });
};
const handleUserTabChange = (key: number) => {
  if (key === 1) {
    getMySubmissionList();
  } else {
    getMyCommentList();
  }
};
const handleUserLogout = async () => {
  if (curUser == null || curUser?.id < 0) {
    return;
  }
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
};
const handleAvatarClick = async () => {
  modalVisible.value = true;
};

const modalVisible = ref(false);

const handleModalClick = () => {
  modalVisible.value = true;
};
const handleModalBeforeOk = async () => {
  if (avatar.value != null) {
    const _file = avatar.value.file;
    // let blob = new Blob([_file], { type: "image/png" });

    const formData = new FormData();
    formData.append("file", _file);

    let res2 = await UserControllerService.updateAvatarUsingPost(formData);
    if (res2.code === 0) {
      Message.error("err" + res2.message);
      return;
    }
  }
};
const headers = {
  "Content-Type": "multipart/form-data",
};
const avatar = ref();
const onChange = (_: any, currentFile: any) => {
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
};
onMounted(async () => {
  curUser.value = store.state.user?.userInfo || {};
  if (curUser.value !== {}) {
    userAvatar.value = curUser.value?.userAvatar;
  } else {
    Message.error("获取用户出错");
  }
  let res = await UserControllerService.getUserRecordByIdUsingGet(
    store.state.user?.userInfo?.id || -1
  );
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  userRecord.value = res.data;
  getMySubmissionList();
});
</script>

<style scoped>
.half-divider {
  min-width: 100%;
  margin: 16px 0;
}
</style>

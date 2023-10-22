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
                'http://127.0.0.1:8102/api/avatars/' +
                curUser.id +
                '/' +
                curUser.userAvatar
              "
            />
          </a-avatar>
          <a-typography-text style="font-size: 17px; margin-left: 20px">
            {{ curUser.userName || "用户" }}
          </a-typography-text>
          <a-button
            type="dashed"
            @click="goToInfoChange"
            style="margin-right: 20px; margin-left: auto; float: right"
            >修改信息
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
          <a-tabs>
            <a-tab-pane key="1">
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
            <a-tab-pane key="2">
              <template #title>
                <icon-message />
                发言记录
              </template>
              Content of Tab Panel 2
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
import { onMounted, ref, reactive } from "vue";
import { SubmissionControllerService, UserControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import { useRouter } from "vue-router";
import {
  IconSelectAll,
  IconMessage,
  IconCodeSquare,
  IconSend,
  IconClockCircle,
  IconSunFill,
  IconQrcode,
  IconSearch,
} from "@arco-design/web-vue/es/icon";
import { SubmissionResultEnums } from "@/components/scripts/enum/SubmissionResultEnums";

const userRecord = ref({});
const router = useRouter();

const curUser = store.state.user?.userInfo;

const titleInput = ref("");
const subCurPage = ref(1);
const subPageSize = ref(8);
const subPageSizes = ref([8, 16, 24]);
const selectedStatus = ref(null);
const data = reactive({
  mySubmissionTable: [],
  mySubmissionCount: 0,
});
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
const handleSubSizeChange = (val: number) => {
  subPageSize.value = val;
  getMySubmissionList();
};
const handleSubCurrentChange = (val: number) => {
  subCurPage.value = val;
  getMySubmissionList();
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
  if (id < 0) {
    return;
  }
  router.push({
    path: "/problem/details/" + id,
  });
};
onMounted(async () => {
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

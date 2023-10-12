<template>
  <div id="problem_details_submissions_view">
    <a-row
      style="
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        margin-top: 30px;
        margin-bottom: 30px;
      "
    >
      <a-select
        v-model="selectedLanguage"
        style="width: 200px"
        placeholder="语言选择"
        @change="getSubmissionList"
      >
        <a-option
          v-for="language of languageEnum"
          :value="language"
          :key="language"
          >{{ language }}
        </a-option>
      </a-select>
      <a-select
        v-model="selectedStatus"
        style="width: 200px; margin-right: 140px"
        placeholder="状态选择"
        @change="getSubmissionList"
      >
        <a-option :value="0"> 提交中</a-option>
        <a-option :value="1"> 编译中</a-option>
        <a-option :value="2"> 运行中</a-option>
        <a-option :value="3"> 判断中</a-option>
        <a-option :value="4" style="color: red"> 失败</a-option>
        <a-option :value="5" style="color: green"> 成功</a-option>
      </a-select>
    </a-row>
    <a-table :data="submissionsData.submissionTable" stripe :pagination="false">
      <template #columns>
        <a-table-column title="结果" :width="120">
          <template #cell="{ record }">
            <a-button
              type="text"
              long
              @click="handleGoToSubmission(record)"
              style="left: 5px"
            >
              <div
                v-if="record.judgeResult.judgeInfo?.resultStr === 'Accepted'"
                style="color: green; font-size: 21px"
              >
                {{ record.judgeResult.judgeInfo?.resultStr || "" }}
              </div>
              <div v-else style="color: red; font-size: 21px">
                {{ record.judgeResult.judgeInfo?.resultStr || "" }}
              </div>
            </a-button>
          </template>
        </a-table-column>
        <a-table-column title="语言" :width="120" align="center">
          <template #cell="{ record }">
            <icon-code-square />
            {{ record.language }}
          </template>
        </a-table-column>
        <a-table-column title="运行用时" :width="120" align="left">
          <template #cell="{ record }">
            <icon-clock-circle />
            <span style="font-size: 20px; margin-left: 10px">
              {{ record.judgeResult?.judgeInfo?.timeUsed || 0 }}
            </span>
            ms
          </template>
        </a-table-column>
        <a-table-column title="内存占用" :width="120" align="left">
          <template #cell="{ record }">
            <icon-qrcode />
            <span style="font-size: 20px; margin-left: 10px">
              {{ record.judgeResult?.judgeInfo?.memoryUsed / 1048576 || 0 }}
            </span>
            MB
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
      :total="submissionsData.submissionCount"
      show-total
      show-jumper
      show-page-size
      :page-size-options="pageSizes"
      @page-size-change="handleSizeChange"
      @change="handleCurrentChange"
      v-model:current="curPage"
      v-model:page-size="pageSize"
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
            type === 'previous' ? { transform: `rotate(180deg)` } : undefined
          "
        />
      </template>
      <template #page-item-ellipsis>
        <icon-sun-fill />
      </template>
    </a-pagination>
    <a-drawer
      :width="700"
      :visible="drawerVisible"
      @ok="drawerVisible = false"
      @cancel="drawerVisible = false"
      unmountOnClose
    >
      <template #title>
        <div
          style="color: green; font-size: 30px"
          v-if="
            submissionDetail.judgeResult.judgeInfo?.resultStr === 'Accepted'
          "
        >
          {{ submissionDetail.judgeResult.judgeInfo?.resultStr || "" }}
        </div>
        <div v-else style="color: red; font-size: 30px">
          {{ submissionDetail.judgeResult.judgeInfo?.resultStr || "" }}
        </div>
      </template>
      <a-row style="margin-bottom: 20px">
        <a-col :span="12">
          运行用时
          <span style="font-size: 20px; margin-left: 10px">
            {{ submissionDetail.judgeResult?.judgeInfo?.timeUsed || 0 }}
          </span>
          ms
        </a-col>
        <a-col :span="12">
          占用内存
          <span style="font-size: 20px; margin-left: 10px">
            {{
              submissionDetail.judgeResult?.judgeInfo?.memoryUsed / 1048576 || 0
            }}
          </span>
          MB
        </a-col>
      </a-row>
      <span
        style="
          font-size: 32px;
          margin-right: 15px;
          margin-top: 10px;
          margin-bottom: 10px;
          font-weight: bold;
        "
        >代码</span
      >
      <span
        style="
          font-size: 26px;
          margin-top: 10px;
          margin-bottom: 10px;
          font-weight: bold;
        "
        >{{ submissionDetail.language || "" }}</span
      >
      <CodeViewer
        :handle-editor-init="handleSubmitCodeInit"
        :handle-lang-init="handleSubmitLangInit"
        :current-code="submissionDetail.code"
        :code-language="submissionDetail.language"
        style="height: 600px; min-height: 600px; width: 100%; margin-top: 20px"
      />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import {
  defineProps,
  inject,
  onMounted,
  reactive,
  ref,
  withDefaults,
} from "vue";
import MdViewer from "@/components/MdViewer.vue";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import moment from "moment/moment";
import {
  IconCheck,
  IconExperiment,
  IconLock,
  IconMinus,
  IconQuestion,
  IconSearch,
  IconSend,
  IconSunFill,
  IconCodeSquare,
  IconClockCircle,
  IconQrcode,
} from "@arco-design/web-vue/es/icon";
import { useRoute, useRouter } from "vue-router";
import { SubmissionControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import store from "@/store";
import { languageEnum } from "@/components/scripts/language/languageEnum";
import * as path from "path";
import CodeEditor from "@/components/CodeEditor.vue";
import CodeViewer from "@/components/CodeViewer.vue";

interface Props {
  id: string;
}

const drawerVisible = ref(false);
const submissionDetail = ref({});

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const route = useRoute();
const router = useRouter();
const curUser = store.state.user?.userInfo;

const handleSubmitCodeInit = () => {
  return submissionDetail.value.code || "";
};
const handleSubmitLangInit = () => {
  return (submissionDetail.value.language as string).toLowerCase() || "";
};
const selectedStatus = ref(null);
const selectedLanguage = ref(null);
let curPage = ref(1);
let pageSize = ref(8);
let pageSizes = ref([8, 16, 24]);
const submissionsData = reactive({
  submissionTable: [],
  submissionCount: 0,
});

const getSubmissionList = async () => {
  let res = await SubmissionControllerService.listMySubmissionVoByPageUsingPost(
    {
      current: curPage.value,
      pageSize: pageSize.value,
      problemId: parseInt(props.id),
      judgeStatus:
        selectedStatus.value === null || curUser.userId < 0
          ? null
          : selectedStatus.value,
      language: selectedLanguage.value === null ? null : selectedLanguage.value,
    }
  );
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  submissionsData.submissionTable = res.data.records || [];
  submissionsData.submissionCount = parseInt(res.data.total);
  if (
    Math.ceil(submissionsData.submissionCount / pageSize.value) < curPage.value
  ) {
    curPage.value = Math.ceil(submissionsData.submissionCount / pageSize.value);
  }
};

const handleGoToSubmission = (record: any) => {
  submissionDetail.value = record;
  drawerVisible.value = true;
};
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  getSubmissionList();
};
const handleCurrentChange = (val: number) => {
  curPage.value = val;
  getSubmissionList();
};

onMounted(() => {
  getSubmissionList();
});

const data = inject("data");
</script>

<style scoped>
.tag_class {
  margin-left: 10px;
}

* {
  overflow: hidden;
}

.background_trans {
  background: transparent;
}

.background_trans * {
  background: transparent;
}
</style>

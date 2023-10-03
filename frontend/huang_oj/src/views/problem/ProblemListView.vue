<template>
  <div id="problem_list_view">
    <a-table :data="data.problemTable" stripe :pagination="false">
      <template #columns>
        <a-table-column title="状态" :width="90">
          <template #cell="{ record }">
            <div
              v-if="
                (curUser?.isVip === 1 && record.isVip === 1) ||
                record.isVip === 0
              "
            >
              <icon-check v-if="record.isSolved === 1" />
              <icon-question v-else-if="record.isSolved === -1" />
              <div style="display: none" v-else />
            </div>

            <icon-lock v-else />
          </template>
        </a-table-column>
        <a-table-column title="题目名称" :width="400" align="center">
          <template #cell="{ record }">
            <a-button
              type="text"
              long
              @click="console.log(record)"
              style="left: 5px"
              >{{ record.id }} {{ record.title }}
            </a-button>
          </template>
        </a-table-column>
        <a-table-column title="通过率" :width="90">
          <template #cell="{ record }">
            {{ (record?.acceptance || 0) * 100 }}%
          </template>
        </a-table-column>
        <a-table-column title="难度" :width="90">
          <template #cell="{ record }">
            <div
              v-if="record.tags.difficulty === 0"
              style="color: rgb(137, 255, 83)"
            >
              简单
            </div>
            <div
              v-else-if="record.tags.difficulty === 1"
              style="color: rgb(229, 192, 44)"
            >
              中等
            </div>
            <div v-else style="color: rgb(255, 103, 83)">困难</div>
          </template>
        </a-table-column>
        <a-table-column title="解答" :width="90">
          <template #cell="{ record }">
            <a-button @click="console.log(record)">
              <template #icon>
                <icon-experiment />
              </template>
            </a-button>
          </template>
        </a-table-column>
        <a-table-column
          title="修改题目"
          :width="300"
          v-if="curUser?.userRole || roleEnum.NOT_LOGIN === roleEnum.ADMIN"
        >
          <template #cell="{ record }">
            <a-button type="primary" @click="handleUpdateProblem(record.id)"
              >修改该题目
            </a-button>
            <a-button
              style="margin-left: 10px"
              status="danger"
              type="primary"
              @click="handleDeleteProblem(record.id)"
              >删除该题目
            </a-button>
          </template>
        </a-table-column>
      </template>
    </a-table>
    <a-pagination
      :total="data.problemCounts"
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
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { ProblemControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import store from "@/store";
import {
  IconLock,
  IconQuestion,
  IconCheck,
  IconExperiment,
  IconSunFill,
  IconSend,
} from "@arco-design/web-vue/es/icon";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { useRouter } from "vue-router";

const router = useRouter();

const curUser = store.state.user?.userInfo;

let curPage = ref(1);
let pageSize = ref(8);
let pageSizes = ref([8, 16, 24]);

const data = reactive({
  problemTable: [],
  problemCounts: 0,
});

const getProblemList = async () => {
  let res = await ProblemControllerService.listProblemVoByPageUsingPost({
    current: curPage.value,
    pageSize: pageSize.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  data.problemTable = res.data.records || [];
  data.problemCounts = parseInt(res.data.total);
};

const handleSizeChange = (val: number) => {
  pageSize.value = val;
  getProblemList();
};
const handleCurrentChange = (val: number) => {
  curPage.value = val;
  getProblemList();
};

async function handleUpdateProblem(id: string) {
  router.push(`/problem/add?edit=${id}`);
}

async function handleDeleteProblem(id: string) {
  let res = await ProblemControllerService.deleteProblemUsingPost({
    id: id,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  } else {
    Message.success("删除成功");
    getProblemList();
  }
}

onMounted(() => {
  getProblemList();
});
</script>

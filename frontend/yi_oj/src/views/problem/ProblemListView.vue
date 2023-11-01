<template>
  <div id="problem_list_view">
    <a-row
      style="
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        margin-top: 30px;
        margin-bottom: 30px;
      "
    >
      <a-col :span="4">
        <a-input
          placeholder="请输入要搜索的内容"
          search-button
          style="width: 99%"
          v-model="titleInput"
          @press-enter="getProblemList"
        >
          <template #button-icon>
            <icon-search />
          </template>
        </a-input>
      </a-col>
      <a-col :span="3" :offset="1">
        <a-select
          v-model="selectedDifficulty"
          style="width: 99%"
          placeholder="难度选择"
          @change="getProblemList"
        >
          <a-option :value="0" style="color: rgb(137, 255, 83)">简单</a-option>
          <a-option :value="1" style="color: rgb(229, 192, 44)">中等</a-option>
          <a-option :value="2" style="color: rgb(255, 103, 83)">困难</a-option>
        </a-select>
      </a-col>
      <a-col :span="3" :offset="1">
        <a-select
          v-model="selectedStatus"
          style="width: 99%"
          placeholder="状态选择"
          @change="getProblemList"
        >
          <a-option :value="-1">
            <icon-question />
            尝试过
          </a-option>
          <a-option :value="0">
            <icon-minus />
            未尝试过
          </a-option>
          <a-option :value="1">
            <icon-check />
            解答出
          </a-option>
        </a-select>
      </a-col>
      <a-col
        :span="2"
        :offset="10"
        v-if="
          curUser?.userRole !== undefined &&
          curUser?.userRole !== null &&
          curUser?.userRole === roleEnum.ADMIN
        "
      >
        <a-button type="primary" @click="handleAddProblem()"
          >添加题目
        </a-button>
      </a-col>
    </a-row>
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
              <a-popover
                position="bottom"
                trigger="hover"
                v-if="record.isSolved === 1"
              >
                <icon-check />
                <template #content> 解答出</template>
              </a-popover>
              <a-popover
                position="bottom"
                trigger="hover"
                v-else-if="record.isSolved === -1"
              >
                <icon-question />
                <template #content> 尝试过</template>
              </a-popover>
              <div style="display: none" v-else />
            </div>

            <a-popover position="bottom" trigger="hover" v-else>
              <icon-lock />
              <template #content> 未解锁</template>
            </a-popover>
          </template>
        </a-table-column>
        <a-table-column title="题目名称" :width="220" align="center">
          <template #cell="{ record }">
            <a-button
              type="text"
              long
              @click="handleGoToProblem(record.id)"
              style="left: 5px"
              >{{ record.id }} {{ record.title }}
            </a-button>
          </template>
        </a-table-column>
        <a-table-column title="标签" :width="150" align="left">
          <template #cell="{ record }">
            <a-tag v-for="item of record.tags" :key="item" color="green"
              >{{ item }}
            </a-tag>
          </template>
        </a-table-column>
        <a-table-column title="通过率" :width="90">
          <template #cell="{ record }">
            {{ ((record?.acceptance || 0) * 100).toFixed(1) }}%
          </template>
        </a-table-column>
        <a-table-column title="创建日期" :width="90">
          <template #cell="{ record }">
            {{ moment(record?.createTime).format("YYYY-MM-DD") }}
          </template>
        </a-table-column>
        <a-table-column title="难度" :width="90">
          <template #cell="{ record }">
            <div v-if="record.difficulty === 0" style="color: green">简单</div>
            <div v-else-if="record.difficulty === 1" style="color: orange">
              中等
            </div>
            <div v-else style="color: firebrick">困难</div>
          </template>
        </a-table-column>
        <a-table-column title="解答" :width="90">
          <template #cell="{ record }">
            <a-button @click="goToSolution(record.id)">
              <template #icon>
                <icon-experiment />
              </template>
            </a-button>
          </template>
        </a-table-column>
        <a-table-column
          title="操作题目"
          :width="300"
          v-if="
            curUser?.userRole !== undefined &&
            curUser?.userRole !== null &&
            curUser?.userRole === roleEnum.ADMIN
          "
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
  IconCheck,
  IconExperiment,
  IconLock,
  IconMinus,
  IconQuestion,
  IconSearch,
  IconSend,
  IconSunFill,
} from "@arco-design/web-vue/es/icon";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { useRouter } from "vue-router";
import moment from "moment";

const router = useRouter();

const selectedStatus = ref(null);
const selectedDifficulty = ref(null);
const curUser = store.state.user?.userInfo;

const titleInput = ref("");
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
    title: titleInput.value,
    status:
      selectedStatus.value === null || curUser.id > -1
        ? null
        : selectedStatus.value,
    difficulty:
      selectedDifficulty.value === null ? null : selectedDifficulty.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  data.problemTable = res.data.records || [];
  data.problemCounts = parseInt(res.data.total);
  if (Math.ceil(data.problemCounts / pageSize.value) < curPage.value) {
    curPage.value = Math.ceil(data.problemCounts / pageSize.value);
  }
};

const goToSolution = (id: string) => {
  router.push({
    path: `/problem/details/${id}/solution`,
  });
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

async function handleAddProblem() {
  router.push(`/problem/add`);
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

async function handleGoToProblem(id: string) {
  router.push({
    path: `/problem/details/${id}`,
  });
}

onMounted(() => {
  getProblemList();
});
</script>

<style scoped>
#problem_list_view {
  width: 80%;
  inset: 0;
  margin: auto;
}
</style>

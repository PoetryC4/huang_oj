<template>
  <div id="user_login_view">
    <a-form
      :model="state.formDataProblem"
      :style="{
        width: '75%',
        right: '0',
        left: '0',
        top: '0',
        bottom: '0',
        margin: '3% auto',
      }"
    >
      <a-form-item field="title" label="标题">
        <a-input
          v-model="state.formDataProblem.title"
          placeholder="请输入标题"
        />
      </a-form-item>
      <a-form-item field="content" tooltip="请输入题目内容" label="题目描述">
        <MdEditor
          :value="state.formDataProblem.content"
          :handle-change="onChangeContent"
          style="min-width: 1000px; height: 300px"
        />
      </a-form-item>
      <a-form-item
        v-for="(judge_case, index) of state.formDataProblem.judgeCase"
        :field="`state.formDataProblem.judgeCase[${index}]`"
        :label="`测试用例-${index}`"
        :key="index"
      >
        <a-input
          v-model="judge_case.input"
          placeholder="样例输入"
          style="
            width: 450px;
            left: 0;
            right: 0;
            bottom: 0;
            top: 0;
            margin: auto auto auto 10px;
          "
        />
        <a-input
          v-model="judge_case.expected"
          placeholder="样例正确输出"
          style="
            width: 450px;
            left: 0;
            right: 0;
            bottom: 0;
            top: 0;
            margin: auto 10px auto auto;
          "
        />
        <a-button
          @click="handleProblemDelete(index)"
          :style="{ marginLeft: '10px' }"
          >删除
        </a-button>
      </a-form-item>
      <a-form-item>
        <a-button
          style="right: 0; left: 0; top: 0; bottom: 0; margin: auto"
          @click="handleProblemAdd"
          >添加样例
        </a-button>
      </a-form-item>
      <a-form-item field="judgeConfig.hint" label="提示">
        <a-input
          v-model="state.formDataProblem.judgeConfig.hint"
          placeholder="请输入提示"
        />
      </a-form-item>
      <a-form-item field="judgeConfig" label="测试数值配置">
        <a-input-number
          v-model="state.formDataProblem.judgeConfig.memoryLimit"
          placeholder="请输入内存限制"
          style="
            width: 250px;
            left: 0;
            right: 0;
            bottom: 0;
            top: 0;
            margin: auto 30px auto auto;
          "
        >
          <template #append> MB内存限制</template>
        </a-input-number>
        <a-input-number
          v-model="state.formDataProblem.judgeConfig.timeLimit"
          placeholder="请输入时间限制"
          style="
            width: 250px;
            left: 0;
            right: 0;
            bottom: 0;
            top: 0;
            margin: auto 30px auto auto;
          "
        >
          <template #append> s时间限制</template>
        </a-input-number>
        <a-input-number
          v-model="state.formDataProblem.judgeConfig.stackLimit"
          placeholder="请输入堆栈限制"
          style="
            width: 250px;
            left: 0;
            right: 0;
            bottom: 0;
            top: 0;
            margin: auto 30px auto auto;
          "
        >
          <template #append> MB堆栈限制</template>
        </a-input-number>
      </a-form-item>
      <a-form-item field="judgeConfig.testCaseProvided" label="提供的样例个数">
        <a-input-number
          v-model="state.formDataProblem.judgeConfig.testCaseProvided"
          placeholder="请输入提供的样例个数"
        />
      </a-form-item>
      <a-form-item field="difficulty" label="难度描述">
        <a-radio-group
          v-model="state.formDataProblem.tags.difficulty"
          type="button"
        >
          <a-radio :value="0">简单</a-radio>
          <a-radio :value="1">中等</a-radio>
          <a-radio :value="2">困难</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item field="tags.types" label="标签">
        <a-input-tag
          v-model="state.formDataProblem.tags.types"
          placeholder="输入题目类型"
        />
      </a-form-item>
      <a-form-item field="solution" tooltip="请输入题目解法" label="题目解法">
        <MdEditor
          :value="state.formDataProblem.solution"
          :handle-change="onChangeSolution"
          style="min-width: 1000px; height: 300px"
        />
      </a-form-item>
      <a-form-item field="vipOnly" label="是否Vip尊享">
        <a-switch v-model="state.formDataProblem.vipOnly" />
      </a-form-item>
      <a-form-item>
        <a-button
          @click="handleProblemSubmit"
          style="right: 0; left: 0; top: 0; bottom: 0; margin: auto"
          ><p v-if="id === ''">添加题目</p>
          <p v-else>修改题目</p>
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { ProblemControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import MdEditor from "@/components/MdEditor.vue";
import store from "@/store";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

const id = ref("");

onMounted(() => {
  id.value = (route.query.edit as string) || "";
  if (id.value !== "") {
    handleProblemFillIn(id.value);
  }
});

const onChangeContent = (v: string) => {
  state.formDataProblem.content = v;
};
const onChangeSolution = (v: string) => {
  state.formDataProblem.solution = v;
};
const handleProblemAdd = () => {
  state.formDataProblem.judgeCase.push({
    expected: "",
    input: "",
  });
};
const handleProblemDelete = (index: number) => {
  state.formDataProblem.judgeCase.splice(index, 1);
};
const state = reactive({
  formDataProblem: {
    content: "",
    vipOnly: false,
    judgeCase: [
      {
        expected: "",
        input: "",
      },
    ],
    judgeConfig: {
      hint: "",
      memoryLimit: 0,
      stackLimit: 0,
      testCaseProvided: 0,
      timeLimit: 0,
    },
    solution: "",
    tags: {
      difficulty: 0,
      types: [],
    },
    title: "",
  },
});

async function handleProblemSubmit() {
  let userId = store.state.user?.userInfo?.userId;
  if (userId === -1) {
    Message.error("未登录");
    return;
  }
  let res;
  if (id.value === "") {
    res = await ProblemControllerService.addProblemUsingPost({
      ...state.formDataProblem,
      userId: userId,
      isVip: state.formDataProblem.vipOnly ? 1 : 0,
    });
    if (res.code !== null && res.code === 1) {
      Message.success("添加成功");
    } else {
      Message.error(res.message || "出错");
    }
  } else {
    res = await ProblemControllerService.updateProblemUsingPost({
      ...state.formDataProblem,
      userId: userId,
      id: id.value,
      isVip: state.formDataProblem.vipOnly ? 1 : 0,
    });
    if (res.code !== null && res.code === 1) {
      Message.success("修改成功");
    } else {
      Message.error(res.message || "出错");
    }
  }
}

async function handleProblemFillIn(id: string) {
  let res;
  res = await ProblemControllerService.getProblemVoByIdUsingGet(id);
  if (res.code !== null && res.code === 1) {
    Message.success("获取成功");
    state.formDataProblem.judgeCase = res.data.judgeCase || [];
    state.formDataProblem.content = res.data.content;
    state.formDataProblem.solution = res.data.solution;
    state.formDataProblem.title = res.data.title;
    state.formDataProblem.tags.difficulty = res.data.tags.difficulty;
    state.formDataProblem.tags.types = res.data.tags.types;
    state.formDataProblem.judgeConfig.hint = res.data.judgeConfig.hint;
    state.formDataProblem.judgeConfig.timeLimit = parseInt(
      res.data.judgeConfig.timeLimit
    );
    state.formDataProblem.judgeConfig.memoryLimit = parseInt(
      res.data.judgeConfig.memoryLimit
    );
    state.formDataProblem.judgeConfig.stackLimit = parseInt(
      res.data.judgeConfig.stackLimit
    );
    state.formDataProblem.judgeConfig.testCaseProvided =
      res.data.judgeConfig.testCaseProvided;
    state.formDataProblem.vipOnly = res.data.isVip === 1;
  } else {
    Message.error(res.message || "出错");
  }
}
</script>

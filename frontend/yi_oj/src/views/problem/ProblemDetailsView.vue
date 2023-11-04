<template>
  <div id="problem_details_view">
    <a-layout style="overflow: hidden">
      <a-layout-sider
        :resize-directions="['right']"
        :collapsible-="true"
        :width="800"
      >
        <a-card style="overflow: hidden">
          <a-tabs
            default-active-key="1"
            lazy-load
            @tab-click="handleSiderTabChange"
            :active-key="siderTabKey"
          >
            <template #extra>
              <a-button type="text" @click="goToProblemList">
                <a-tag color="gray" style="font-size: 15px">
                  <template #icon>
                    <icon-list style="font-size: 23px; stroke-width: 6px" />
                  </template>
                  题目列表
                </a-tag>
              </a-button>
            </template>
            <a-tab-pane key="1" title="题目详情">
              <a-row justify="space-between">
                <a-col :span="8" style="z-index: 100">
                  <h1>
                    {{ data.problem?.id || "" }}.{{ data.problem?.title || "" }}
                  </h1>
                </a-col>
                <a-col :span="4" class="background_trans">
                  <a-popover
                    position="bottom"
                    style="width: 400px"
                    trigger="click"
                  >
                    <a-button
                      :style="{
                        width: '100px',
                      }"
                      >提示
                    </a-button>
                    <template #content>
                      {{ data.problem?.judgeConfig?.hint || "" }}
                    </template>
                  </a-popover>
                </a-col>
              </a-row>
              <a-divider />
              <a-space />
              <a-tag class="tag_class">
                时间限制:{{ data.problem.judgeConfig?.timeLimit || 1000 }}ms
              </a-tag>
              <a-tag class="tag_class">
                空间限制:{{ data.problem.judgeConfig?.memoryLimit || 1000 }}MB
              </a-tag>
              <a-tag class="tag_class">
                堆栈限制:{{ data.problem.judgeConfig?.stackLimit || 1000 }}MB
              </a-tag>
              <a-tag
                color="green"
                style="float: right; cursor: pointer; margin-right: 20px"
                @click="handleLikeClick"
              >
                <template #icon>
                  <icon-thumb-up-fill v-if="isLiked" />
                  <icon-thumb-up v-else />
                </template>
                <span style="font-weight: bold">{{
                  data.problem.thumbNum
                }}</span>
              </a-tag>
              <a-tag
                color="red"
                style="float: right; cursor: pointer; margin-right: 20px"
                @click="handleDislikeClick"
              >
                <template #icon>
                  <icon-thumb-down-fill v-if="isDisliked" />
                  <icon-thumb-down v-else />
                </template>
                <span style="font-weight: bold">{{
                  data.problem.disLikeNum
                }}</span>
              </a-tag>
              <a-divider />
              <a-space />
              <a-tag
                v-if="data.problem.difficulty === 0"
                color="green"
                class="tag_class"
              >
                简单
              </a-tag>
              <a-tag
                v-else-if="data.problem.difficulty === 1"
                color="orange"
                class="tag_class"
              >
                中等
              </a-tag>
              <a-tag v-else color="red" class="tag_class">困难</a-tag>
              <a-tag v-if="data.problem.isSolved === 1" class="tag_class">
                <template #icon>
                  <icon-check />
                </template>
                <div style="color: rgb(93, 217, 40)">解答出</div>
              </a-tag>
              <a-tag v-else-if="data.problem.isSolved === -1" class="tag_class">
                <template #icon>
                  <icon-question />
                </template>
                <div style="color: rgb(229, 192, 44)">尝试过</div>
              </a-tag>
              <a-tag
                v-for="item of data.problem.tags"
                :key="item"
                color="gray"
                class="tag_class"
                >{{ item }}
              </a-tag>
              <MdViewer
                :value="data.problem?.content || ''"
                style="margin-top: 40px"
              />
              <a-tag class="tag_class">
                提交数:
                <span
                  style="font-size: 20px; font-weight: bold; margin-left: 15px"
                  >{{ formatNum(data.problem?.submittedCount || 0) }}</span
                >
              </a-tag>
              <a-tag class="tag_class">
                通过数:
                <span
                  style="font-size: 20px; font-weight: bold; margin-left: 15px"
                  >{{ formatNum(data.problem?.accpetedCount || 0) }}</span
                >
              </a-tag>
              <a-tag class="tag_class">
                通过率:
                <span
                  style="font-size: 20px; font-weight: bold; margin-left: 15px"
                  >{{ (data.problem?.acceptance * 100).toFixed(1) || 0 }}%</span
                >
              </a-tag>
            </a-tab-pane>
            <a-tab-pane key="2" title="题目解法">
              <router-view></router-view>
            </a-tab-pane>
            <a-tab-pane key="3" title="评论区">
              <router-view></router-view>
            </a-tab-pane>
            <a-tab-pane key="4" title="提交历史">
              <router-view :key="routerKey"></router-view>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-layout-sider>
      <a-layout>
        <a-row :gutter="2">
          <a-col>
            <CodeEditor
              :handle-editor-init="doInitCode"
              :next-code="nextCode"
              :code-language="codeLanguage"
              :current-code="userCode"
              :handle-change="onChangeCode"
              :handle-language-change="doLanguageChange"
              style="height: 600px"
            />
          </a-col>
        </a-row>
        <a-row :gutter="1">
          <a-col>
            <a-collapse :active-key="[collapseController]" style="cursor: text">
              <a-collapse-item :key="1" style="cursor: text">
                <template #expand-icon>
                  <div
                    style="cursor: pointer"
                    @click="
                      collapseController = collapseController === 1 ? -1 : 1
                    "
                  >
                    <icon-right v-if="collapseController !== 1" />
                    <icon-down v-else />
                  </div>
                </template>
                <template #header>
                  <div
                    style="cursor: pointer"
                    @click="
                      collapseController = collapseController === 1 ? -1 : 1
                    "
                  >
                    调试
                  </div>
                </template>
                <template #extra>
                  <a-button
                    :disabled="isJudge"
                    @click.stop="handleSwitchSourceMode"
                    style="margin-right: 40px"
                    type="text"
                    >{{ isSourceMode ? "tab模式" : "源码模式" }}
                  </a-button>
                  <a-button
                    :disabled="isJudge"
                    @click.stop="handleCodeTest"
                    style="margin-right: 40px"
                    >运行
                  </a-button>
                  <a-button
                    :disabled="isJudge"
                    type="primary"
                    @click.stop="handleCodeSubmit"
                    style="margin-right: 30px"
                    >提交
                  </a-button>
                </template>
                <a-tabs
                  default-active-key="1"
                  :active-key="codeTestTabKey"
                  @change="handleCodeTestTabChange"
                >
                  <a-tab-pane key="1" title="测试用例">
                    <div v-if="!isSourceMode">
                      <a-tag
                        v-for="(example, index) of exampleTags"
                        :key="index"
                        :closable="exampleTags.length !== 1"
                        :visible="true"
                        @close="handleExampleRemove(index)"
                        @click="handleExampleTagClick(index)"
                        style="cursor: pointer"
                      >
                        样例{{ index + 1 }}
                      </a-tag>
                      <a-tag
                        :style="{
                          width: '90px',
                          backgroundColor: 'var(--color-fill-2)',
                          border: '1px dashed var(--color-fill-3)',
                          cursor: 'pointer',
                        }"
                        @click="handleExampleAdd"
                      >
                        <template #icon>
                          <icon-plus />
                        </template>
                        增加样例
                      </a-tag>
                      <div
                        v-for="(name, index) of exampleTagSelected"
                        :key="index"
                      >
                        <a-typography-paragraph>
                          {{ data.problem.functionConfig.varNames[index] }} =
                        </a-typography-paragraph>
                        <a-input
                          v-model="exampleTagSelected[index]"
                          :placeholder="
                            '请输入' +
                            data.problem.functionConfig.varNames[index] +
                            '的值'
                          "
                          :rules="[{ required: true, message: '值不得为空' }]"
                          :validate-trigger="['change', 'input']"
                        />
                      </div>
                    </div>
                    <div v-else>
                      <a-mention
                        v-model="testInput"
                        type="textarea"
                        placeholder="请输入测试用例"
                        style="min-height: 100px"
                      />
                    </div>
                  </a-tab-pane>
                  <a-tab-pane key="2" title="测试结果">
                    <div v-if="isJudge">
                      <a-skeleton :animation="true">
                        <a-space
                          direction="vertical"
                          :style="{ width: '100%' }"
                          size="large"
                        >
                          <a-skeleton-shape />
                          <a-skeleton-line :rows="3" />
                        </a-space>
                      </a-skeleton>
                    </div>
                    <div v-else>
                      <div v-if="userSubmissionRes.judgeInfo != undefined">
                        <span
                          v-if="
                            userSubmissionRes.judgeInfo?.resultStr ===
                            'Accepted'
                          "
                          :style="{ color: 'green', fontSize: '20px' }"
                        >
                          {{ userSubmissionRes.judgeInfo?.resultStr || "" }}
                        </span>
                        <span
                          v-else
                          :style="{ color: 'red', fontSize: '20px' }"
                        >
                          {{ userSubmissionRes.judgeInfo?.resultStr || "" }}
                        </span>
                        <span
                          :style="{ fontSize: '16px', marginLeft: '20px' }"
                          v-if="
                            userSubmissionRes.judgeInfo?.timeUsed != undefined
                          "
                        >
                          运行用时:
                          {{
                            userSubmissionRes.judgeInfo?.timeUsed || "NaN"
                          }}ms</span
                        >
                        <span
                          :style="{ fontSize: '16px', marginLeft: '20px' }"
                          v-if="
                            userSubmissionRes.judgeInfo?.memoryUsed != undefined
                          "
                        >
                          占用内存:
                          {{
                            (
                              userSubmissionRes.judgeInfo?.memoryUsed / 1048576
                            ).toFixed(1) || "NaN"
                          }}MB</span
                        >
                        <a-tabs default-active-key="0" type="round">
                          <a-tab-pane
                            v-for="(
                              judgeCaseVO, index
                            ) of userSubmissionRes.judgeCaseVOList"
                            :key="index"
                          >
                            <template #title>
                              <a-tag
                                size="large"
                                style="margin-right: 20px"
                                @click="testResultTagSelect = index"
                                :color="
                                  testResultTagSelect === index
                                    ? 'arcoblue'
                                    : 'gray'
                                "
                                >样例 {{ index + 1 }}
                              </a-tag>
                            </template>

                            <h3>样例输入</h3>
                            <a-alert type="normal">
                              <div style="white-space: pre-wrap">
                                {{ judgeCaseVO.input as string }}
                              </div>
                            </a-alert>
                            <h3 v-if="judgeCaseVO.stdout?.length > 0">
                              样例标准输出
                            </h3>
                            <a-alert type="normal">
                              <div style="white-space: pre-wrap">
                                {{ judgeCaseVO.stdout as string }}
                              </div>
                            </a-alert>
                            <h3>样例返回</h3>
                            <a-alert
                              type="normal"
                              v-if="judgeCaseVO.result === 'Accepted'"
                            >
                              <div
                                :style="{
                                  color: 'green',
                                  whitespace: 'pre-wrap',
                                }"
                              >
                                {{ judgeCaseVO.output as string }}
                              </div>
                            </a-alert>
                            <a-alert type="normal" v-else>
                              <div
                                :style="{
                                  color: 'red',
                                  whitespace: 'pre-wrap',
                                }"
                              >
                                {{ judgeCaseVO.output as string }}
                              </div>
                            </a-alert>
                            <h3>样例期望值</h3>
                            <a-alert type="normal">
                              <div
                                :style="{
                                  color: 'green',
                                  whitespace: 'pre-wrap',
                                }"
                              >
                                {{ judgeCaseVO.expected as string }}
                              </div>
                            </a-alert>
                          </a-tab-pane>
                        </a-tabs>
                      </div>
                      <div style="inset: 0; margin: auto" v-else>
                        请先运行你的代码
                      </div>
                    </div>
                  </a-tab-pane>
                </a-tabs>
              </a-collapse-item>
            </a-collapse>
          </a-col>
        </a-row>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, provide, reactive, ref } from "vue";
import {
  JudgeResult,
  ProblemControllerService,
  ProblemVO,
  SubmissionControllerService,
} from "@/api";
import { Message } from "@arco-design/web-vue";
import store from "@/store";
import {
  IconCheck,
  IconDown,
  IconList,
  IconPlus,
  IconQuestion,
  IconRight,
  IconThumbDown,
  IconThumbDownFill,
  IconThumbUp,
  IconThumbUpFill,
} from "@arco-design/web-vue/es/icon";
import { languageEnum } from "@/components/scripts/enum/LanguageEnum";
import { useRoute, useRouter } from "vue-router";
import { defineProps, withDefaults } from "vue/dist/vue";
import MdViewer from "@/components/MdViewer.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { formatNum } from "@/components/scripts/utils";

const router = useRouter();
const route = useRoute();

const curUser = store.state.user?.userInfo;

const userSubmissionRes = ref<JudgeResult>({});

const routerKey = ref(0);
const nextCode = ref("");
const userCode = ref("");
const codeLanguage = ref("java");
let userCodes = {};
const isLiked = ref(false);
const isDisliked = ref(false);
const isJudge = ref(false);
const collapseController = ref(-1);
const exampleTagChosen = ref(0);
const testInput = ref("");
const exampleTags = ref([]);
const isSourceMode = ref(false);
const exampleTagSelected = ref([]);

const handleSwitchSourceMode = () => {
  isSourceMode.value = !isSourceMode.value;
  examplesSync(isSourceMode.value);
};

const examplesSync = (updateSource: boolean) => {
  if (updateSource) {
    let newTestInput = "start";
    for (let i = 0; i < exampleTags.value.length; i++) {
      for (let j = 0; j < exampleTags.value[i].length; j++) {
        newTestInput =
          newTestInput === "start"
            ? exampleTags.value[i][j] + "\n"
            : newTestInput + exampleTags.value[i][j] + "\n";
      }
    }
    testInput.value = newTestInput;
  } else {
    let strs = testInput.value.split("\n");
    let i = 0;
    exampleTags.value = [];
    while (i < strs.length - 1) {
      let newExample = [];
      for (
        let j = 0;
        j < (data.problem.functionConfig.varCount || 0);
        j++, i++
      ) {
        newExample.push(strs[i]);
      }
      exampleTags.value.push(newExample);
    }
    nextTick();
  }
};

const handleExampleTagClick = (idx: number) => {
  exampleTags.value[exampleTagChosen.value] = exampleTagSelected.value;
  exampleTagSelected.value = exampleTags.value[idx];
  exampleTagChosen.value = idx;
  nextTick();
  console.log(exampleTags.value);
};

const handleExampleRemove = (idx: number) => {
  exampleTags.value.splice(idx, 1);
  exampleTagChosen.value = Math.min(
    exampleTagChosen.value,
    exampleTags.value.length - 1
  );
  exampleTagSelected.value = exampleTags.value[exampleTagChosen.value];
  nextTick();
};

const handleExampleAdd = () => {
  if (exampleTags.value.length >= 8) return;
  let newExample: string[] = [];
  for (let j = 0; j < (data.problem.functionConfig.varCount || 0); j++) {
    newExample.push(exampleTags.value[exampleTags.value.length - 1][j]);
  }
  exampleTags.value.push(newExample);
  nextTick();
  handleExampleTagClick(exampleTags.value.length - 1);
};

const handleLikeClick = async () => {
  if (curUser == null || curUser.id < 0) {
    Message.info("你需要先登录");
    return;
  }
  isLiked.value = !isLiked.value;
  const res = await ProblemControllerService.doLikeProblemUsingPost({
    problemId: data.problem?.id,
  });
  if (res.code == 0) {
    Message.error("err" + res.message);
  } else {
    if (isLiked.value) {
      (data.problem.thumbNum as number)++;
    } else {
      (data.problem.thumbNum as number)--;
    }
  }
};
const handleDislikeClick = async () => {
  if (curUser == null || curUser.id < 0) {
    Message.info("你需要先登录");
    return;
  }
  isDisliked.value = !isDisliked.value;
  const res = await ProblemControllerService.doDislikeProblemUsingPost({
    problemId: data.problem?.id,
  });
  if (res.code == 0) {
    Message.error("err" + res.message);
  } else {
    if (isDisliked.value) {
      (data.problem.disLikeNum as number)++;
    } else {
      (data.problem.disLikeNum as number)--;
    }
  }
};

const codeTestTabKey = ref("1");
const handleCodeTestTabChange = (v: string) => {
  codeTestTabKey.value = v;
};
const siderTabKey = ref("1");
const testResultTagSelect = ref(0);

const goToProblemList = () => {
  router.push({
    path: "/problem/list",
  });
};
const handleSiderTabChange = (v: string) => {
  siderTabKey.value = v;
  doSiderTabChange();
};
const doSiderTabChange = () => {
  switch (siderTabKey.value) {
    case "1": {
      router.push({
        path: `/problem/details/` + props.id,
      });
      break;
    }
    case "2": {
      router.push({
        path: `/problem/details/` + props.id + `/solution`,
      });
      break;
    }
    case "3": {
      router.push({
        path: `/problem/details/` + props.id + `/comments`,
      });
      break;
    }
    case "4": {
      router.push({
        path: `/problem/details/` + props.id + `/submissions`,
      });
      break;
    }
    default: {
      break;
    }
  }
};
const onChangeCode = (v: string) => {
  userCode.value = v;
};
const doInitCode = () => {
  testInput.value = data.problem.judgeCases.input || "";
  userCode.value = userCodes[codeLanguage.value] || "";
  return userCode.value;
};
const doLanguageChange = (v: string) => {
  userCodes[codeLanguage.value] = userCode.value;
  if (userCodes[v] != null && userCodes[v] != "") {
    nextCode.value = userCodes[v];
  } else {
    nextCode.value = "";
  }
  codeLanguage.value = v;
  return nextCode.value;
};

const data = reactive({
  problem: {},
});

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const getProblemDetails = async () => {
  let res = await ProblemControllerService.getProblemVoByIdUsingGet(
    parseInt(props.id)
  );
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  data.problem = res.data as ProblemVO;
  userCodes = data.problem.functionConfig.defaultCode || {};
  isLiked.value = data.problem.isLiked;
  isDisliked.value = data.problem.isDisliked;
};

async function handleCodeTest() {
  if (!curUser || curUser.id === -1) {
    router.push({
      path: `/user/login`,
      query: {
        redirect: route.path,
      },
    });
    return;
  }
  if (isJudge.value) return;
  isJudge.value = true;
  examplesSync(true);
  collapseController.value = 1;
  codeTestTabKey.value = "2";
  const res = await SubmissionControllerService.testSubmitUsingPost({
    judgeCases: {
      input: testInput.value,
    },
    problemSubmitQuest: {
      problemId: data.problem?.id || -1,
      userId: curUser.id,
      language: languageEnum[codeLanguage.value] || "",
      code: userCode.value,
    },
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    isJudge.value = false;
    return;
  } else {
    Message.success("测试提交成功");
    userSubmissionRes.value = res.data;
    isJudge.value = false;
  }
}

async function handleCodeSubmit() {
  if (!curUser || curUser.id === -1) {
    router.push({
      path: `/user/login`,
      query: {
        redirect: route.path,
      },
    });
    return;
  }
  if (isJudge.value) return;
  isJudge.value = true;
  collapseController.value = 1;
  codeTestTabKey.value = "2";
  const res = await SubmissionControllerService.doSubmitUsingPost({
    problemId: data.problem?.id || -1,
    userId: curUser.id,
    language: languageEnum[codeLanguage.value] || "",
    code: userCode.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    isJudge.value = false;
    return;
  } else {
    Message.success("提交成功");
    isJudge.value = false;
    userSubmissionRes.value = res.data.judgeResult;
    getProblemDetails();
    routerKey.value++;
    if (userSubmissionRes.value.judgeInfo?.resultStr === "Accepted") {
      handleSiderTabChange("4");
    }
  }
}

onMounted(async () => {
  let path = route.path;
  if (path.endsWith("/submissions")) {
    siderTabKey.value = "4";
    doSiderTabChange();
  } else if (path.endsWith("/comments")) {
    siderTabKey.value = "3";
    doSiderTabChange();
  } else if (path.endsWith("/solution")) {
    siderTabKey.value = "2";
    doSiderTabChange();
  } else {
    siderTabKey.value = "1";
    doSiderTabChange();
  }
  await getProblemDetails();
  doInitCode();
  doLanguageChange("java");
  examplesSync(false);
  exampleTagSelected.value = exampleTags.value[0];
});
provide("data", data);
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

.problem_details_view {
  overflow: hidden;
}
</style>

<style>
.arco-collapse-item-header {
  cursor: default;
}

.arco-collapse-item-header-left {
  cursor: default;
}
</style>

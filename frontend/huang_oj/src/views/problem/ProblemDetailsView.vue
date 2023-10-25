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
                    <a-mention
                      v-model="testInput"
                      type="textarea"
                      placeholder="请输入测试用例"
                      style="min-height: 100px"
                    />
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
                          :style="{ fontSize: '16px' }"
                          v-if="
                            userSubmissionRes.judgeInfo?.timeUsed != undefined
                          "
                        >
                          运行用时:
                          {{
                            userSubmissionRes.judgeInfo?.timeUsed || ""
                          }}ms</span
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
                                @click="testTagSelect = index"
                                :color="
                                  testTagSelect === index ? 'arcoblue' : 'gray'
                                "
                                >样例 {{ index + 1 }}
                              </a-tag>
                            </template>

                            <h3>样例输入</h3>
                            <a-alert type="normal">
                              <div
                                v-html="(judgeCaseVO.input as string).replace(/\n/g, '<br>')"
                              />
                            </a-alert>
                            <h3 v-if="judgeCaseVO.stdout?.length > 0">
                              样例标准输出
                            </h3>
                            <a-alert type="normal">
                              <div
                                v-html="(judgeCaseVO.stdout as string).replace(/\n/g, '<br>')"
                              />
                            </a-alert>
                            <h3>样例返回</h3>
                            <a-alert
                              type="normal"
                              v-if="judgeCaseVO.result === 'Accepted'"
                            >
                              <div
                                :style="{ color: 'green' }"
                                v-html="(judgeCaseVO.output as string).replace(/\n/g, '<br>')"
                              />
                            </a-alert>
                            <a-alert type="normal" v-else>
                              <div
                                :style="{ color: 'red' }"
                                v-html="(judgeCaseVO.output as string).replace(/\n/g, '<br>')"
                              />
                            </a-alert>
                            <h3>样例期望值</h3>
                            <a-alert type="normal">
                              <div
                                :style="{ color: 'green' }"
                                v-html="(judgeCaseVO.expected as string).replace(/\n/g, '<br>')"
                              />
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
import { onMounted, reactive, provide, ref } from "vue";
import {
  BaseResponse_ProblemVO_,
  JudgeResult,
  ProblemControllerService,
  ProblemVO,
  SubmissionControllerService,
} from "@/api";
import { Message } from "@arco-design/web-vue";
import store from "@/store";
import {
  IconLock,
  IconQuestion,
  IconCheck,
  IconExperiment,
  IconThumbUp,
  IconThumbDown,
  IconThumbUpFill,
  IconThumbDownFill,
  IconBackward,
  IconList,
  IconRight,
  IconDown,
} from "@arco-design/web-vue/es/icon";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { languageEnum } from "@/components/scripts/enum/languageEnum";
import { useRoute, useRouter } from "vue-router";
import moment from "moment";
import { defineProps, withDefaults } from "vue/dist/vue";
import MdViewer from "@/components/MdViewer.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import { formatNum } from "@/components/scripts/utils";
import { languages } from "monaco-editor";

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
const testInput = ref("");
const siderTabKey = ref("1");
const testTagSelect = ref(0);

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
  testInput.value = data.problem.judgeCases.input;
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
</style>

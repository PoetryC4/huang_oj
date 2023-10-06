<template>
  <div id="problem_details_view">
    <a-layout style="overflow: hidden">
      <a-layout-sider
        :resize-directions="['right']"
        :collapsible-="true"
        :width="800"
      >
        <a-card style="overflow: hidden">
          <a-tabs default-active-key="1" lazy-load>
            <a-tab-pane key="1" title="题目详情">
              <a-row justify="space-between">
                <a-col :span="8" style="z-index: 100">
                  <h1>
                    {{ data.problem?.id || "" }}.{{ data.problem?.title || "" }}
                  </h1>
                </a-col>
                <a-col :span="4" class="background_trans">
                  <a-popover position="bottom" content-style="width:400px;">
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
                过题数:
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
              <MdViewer
                :value="data.problem?.solution || ''"
                style="margin-top: 40px"
              />
            </a-tab-pane>
            <a-tab-pane key="3" title="评论区">
              Content of Tab Panel 3
            </a-tab-pane>
            <a-tab-pane key="4" title="提交历史">
              Content of Tab Panel 4
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-layout-sider>
      <a-layout>
        <a-row :gutter="2">
          <a-col>
            <CodeEditor
              :code-language="codeLanguage"
              :value="userCode"
              :handle-change="onChangeCode"
              :handle-language-change="doLanguageChange"
              style="height: 600px"
            />
          </a-col>
        </a-row>
        <a-row :gutter="1">
          <a-col>
            <a-collapse>
              <a-collapse-item header="调试" key="1">
                <template #extra>
                  <a-button
                    @click.stop="handleCodeTest"
                    style="margin-left: 40px"
                    >运行
                  </a-button>
                  <a-button
                    type="primary"
                    @click.stop="handleCodeSubmit"
                    style="margin-left: 40px; margin-right: 30px"
                    >提交
                  </a-button>
                </template>
                <a-tabs default-active-key="1">
                  <a-tab-pane key="1" title="测试用例">
                    <a-mention
                      v-model="testInput"
                      type="textarea"
                      placeholder="请输入测试用例"
                    />
                  </a-tab-pane>
                  <a-tab-pane key="2" title="测试结果">
                    Content of Tab Panel 2
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
import { onMounted, reactive, ref } from "vue";
import {
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
} from "@arco-design/web-vue/es/icon";
import { roleEnum } from "@/components/scripts/access/roleEnum";
import { languageEnum } from "@/components/scripts/language/languageEnum";
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

const userCode = ref("");
const codeLanguage = ref("");

const onChangeCode = (v: string) => {
  userCode.value = v;
};
const doLanguageChange = (v: string) => {
  codeLanguage.value = v;
};
const testInput = ref("");

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
};

async function handleCodeTest() {
  console.log(testInput.value); // TODO 代码测试
}

async function handleCodeSubmit() {
  if (!curUser || curUser.userId === -1) {
    router.push({
      path: `/user/login`,
      query: {
        redirect: route.path,
      },
    });
    return;
  }
  const res = await SubmissionControllerService.doSubmitUsingPost({
    problemId: data.problem?.id || -1,
    userId: curUser.userId,
    language: languageEnum[codeLanguage.value] || "",
    code: userCode.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  } else {
    Message.success("提交成功");
  }
}

onMounted(() => {
  getProblemDetails();
  doLanguageChange("python");
});
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

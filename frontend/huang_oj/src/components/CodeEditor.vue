<template>
  <div id="code_editor">
    <a-select
      style="margin-left: 50px"
      v-model="langValue"
      :style="{ width: '180px' }"
      placeholder="选择一门编程语言"
      @change="handleSelectChange"
    >
      <a-option
        v-for="item of langs"
        :value="item.name"
        :label="item.fancyName"
        :key="item"
      />
    </a-select>
    <div id="code_editor_inset" ref="codeEditorRef"></div>
  </div>
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import { defineProps, onMounted, ref, toRaw, withDefaults } from "vue";

const codeEditorRef = ref();
const codeEditor = ref();
const langValue = ref("python");
const langs = [
  {
    name: "bat",
    fancyName: "bat",
  },
  {
    name: "cpp",
    fancyName: "C++",
  },
  {
    name: "go",
    fancyName: "GoLang",
  },
  {
    name: "java",
    fancyName: "Java",
  },
  {
    name: "javascript",
    fancyName: "JavaScript",
  },
  {
    name: "kotlin",
    fancyName: "Kotlin",
  },
  {
    name: "mysql",
    fancyName: "MySQL",
  },
  {
    name: "php",
    fancyName: "PHP",
  },
  {
    name: "python",
    fancyName: "Python",
  },
  {
    name: "rust",
    fancyName: "rust",
  },
  {
    name: "shell",
    fancyName: "shell",
  },
  {
    name: "sql",
    fancyName: "SQL",
  },
  {
    name: "typescript",
    fancyName: "TypeScript",
  },
];

interface Props {
  value: string;
  handleChange: (v: string) => void;
}

const props = withDefaults(defineProps<Props>(), {
  value: () => "",
  handleChange: (v: string) => {
    console.log(v);
  },
  handleSelectChange: (v: string) => {
    console.log(v);
  },
});

async function handleSelectChange(v: string) {
  langValue.value = v;
  monaco.editor.setModelLanguage(
    toRaw(codeEditor.value).getModel(),
    langValue.value
  );
}

onMounted(() => {
  // 初始化编辑器，确保dom已经渲染
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value, //编辑器初始显示文字
    language: langValue.value, //此处使用的python，其他语言支持自行查阅demo
    theme: "vs", //官方自带三种主题vs, hc-black, or vs-dark
    selectOnLineNumbers: true, //显示行号
    roundedSelection: false,
    readOnly: false, // 只读
    cursorStyle: "line", //光标样式
    automaticLayout: true, //自动布局
    glyphMargin: true, //字形边缘
    useTabStops: false,
    fontSize: 15, //字体大小
    autoIndent: "advanced", //自动布局
    quickSuggestionsDelay: 100, //代码提示延时
    minimap: {
      enabled: true,
    },
  });
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
</script>

<style scoped>
#code_editor_inset {
  min-height: 400px;
}
</style>

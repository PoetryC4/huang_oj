<template>
  <div id="code_viewer">
    <div
      id="code_viewer_inset"
      ref="codeViewerRef"
      style="min-height: 600px"
    ></div>
  </div>
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import {
  defineProps,
  onMounted,
  ref,
  toRaw,
  withDefaults,
  defineEmits,
  computed,
} from "vue";

const codeViewerRef = ref();
const codeEditor = ref();
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
  codeLanguage: string;
  currentCode: string;
  handleEditorInit: () => string;
  handleLangInit: () => string;
}

interface Emits {
  (e: "update:codeLanguage", v: string): void;
}

const props = withDefaults(defineProps<Props>(), {
  codeLanguage: "java",
  currentCode: () => "",
  handleEditorInit: () => {
    return "";
  },
  handleLangInit: () => {
    return "";
  },
});
const emit = defineEmits<Emits>();
const language = computed({
  get() {
    return props.codeLanguage;
  },
  set(v: string) {
    emit("update:codeLanguage", v);
  },
});

onMounted(() => {
  // 初始化编辑器，确保dom已经渲染
  codeEditor.value = monaco.editor.create(codeViewerRef.value, {
    value: props.currentCode, //编辑器初始显示文字
    language: props.codeLanguage, //此处使用的python，其他语言支持自行查阅demo
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
});
onMounted(() => {
  setTimeout(() => {
    let initCode = props.handleEditorInit();
    let langS = props.handleLangInit();
    if (initCode != null && initCode != "") {
      toRaw(codeEditor.value).setValue(initCode);
    } else {
      toRaw(codeEditor.value).setValue("");
    }
    monaco.editor.setModelLanguage(
      toRaw(codeEditor.value).getModel(),
      langS.toLowerCase()
    );
  }, 500);
});
</script>

<style scoped>
#code_editor_inset {
  min-height: 400px;
}
</style>

<template>
  <div id="code_editor">
    <a-select
      style="margin-left: 50px"
      v-model="language"
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
    <div
      id="code_editor_inset"
      ref="codeEditorRef"
      style="min-height: 600px"
    ></div>
  </div>
</template>

<script setup lang="ts">
import * as monaco from "monaco-editor";
import {
  computed,
  defineEmits,
  defineProps,
  onMounted,
  ref,
  toRaw,
  withDefaults,
} from "vue";

const codeEditorRef = ref();
const codeEditor = ref();
const langs = [
  /*
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
  },*/
  {
    name: "java",
    fancyName: "Java",
  } /*
  {
    name: "kotlin",
    fancyName: "Kotlin",
  },
  {
    name: "c",
    fancyName: "C",
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
  }
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
  },*/,
  {
    name: "javascript",
    fancyName: "JavaScript",
  },
];

interface Props {
  codeLanguage: string;
  currentCode: string;
  nextCode: string;
  handleChange: (v: string) => void;
  handleLanguageChange: (v: string) => string;
  handleEditorInit: () => string;
}

interface Emits {
  (e: "update:codeLanguage", v: string): void;
}

const props = withDefaults(defineProps<Props>(), {
  codeLanguage: "java",
  currentCode: () => "",
  nextCode: () => "",
  handleChange: (v: string) => {
    console.log(v);
  },
  handleLanguageChange: (v: string) => {
    console.log(v);
    return "";
  },
  handleEditorInit: () => {
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

async function handleSelectChange(v: string) {
  let next_code = props.handleLanguageChange(v);
  emit("update:codeLanguage", v);
  monaco.editor.setModelLanguage(
    toRaw(codeEditor.value).getModel(),
    language.value.toLowerCase()
  );
  if (next_code != null && next_code != "") {
    toRaw(codeEditor.value).setValue(next_code);
  } else {
    toRaw(codeEditor.value).setValue("");
  }
}

onMounted(() => {
  // 初始化编辑器，确保dom已经渲染
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
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
  codeEditor.value.onDidChangeModelContent(() => {
    props.handleChange(toRaw(codeEditor.value).getValue());
  });
});
onMounted(() => {
  setTimeout(() => {
    let initCode = props.handleEditorInit();
    if (initCode != null && initCode != "") {
      toRaw(codeEditor.value).setValue(initCode);
    } else {
      toRaw(codeEditor.value).setValue("");
    }
  }, 500);
});
</script>

<style scoped>
#code_editor_inset {
  min-height: 400px;
}
</style>

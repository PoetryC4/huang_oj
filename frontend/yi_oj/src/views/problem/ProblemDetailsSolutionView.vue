<template>
  <div id="problem_details_solution_view">
    <MdViewer :value="data.problem?.solution || ''" style="margin-top: 40px" />
  </div>
</template>

<script setup lang="ts">
import { inject } from "vue";
import MdViewer from "@/components/MdViewer.vue";

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

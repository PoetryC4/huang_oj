<template>
  <div id="problem_details_submission_view">历史</div>
</template>

<script setup lang="ts">
import { defineProps, inject, onMounted, withDefaults } from "vue";

const data = inject("data");

interface Props {
  id: string;
  subId: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
  subId: () => "",
});
onMounted(() => {
  // console.log(props.id);
  // console.log(props.subId);
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

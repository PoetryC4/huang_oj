<template>
  <div id="problem_details_comments_view">评论区</div>
</template>

<script setup lang="ts">
import { inject } from "vue";

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

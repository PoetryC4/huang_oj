<template>
  <div id="problem_details_comments_view">
    <a-comment
      author="Socrates"
      content="Comment body content."
      datetime="1 hour"
    >
      <template #actions>
        <span class="action" key="heart" @click="onLikeChange">
          <span v-if="like">
            <IconHeartFill :style="{ color: '#f53f3f' }" />
          </span>
          <span v-else>
            <IconHeart />
          </span>
          {{ 83 + (like ? 1 : 0) }}
        </span>
        <span class="action" key="star" @click="onStarChange">
          <span v-if="star">
            <IconStarFill
              style="
                 {
                  color: '#ffb400';
                }
              "
            />
          </span>
          <span v-else>
            <IconStar />
          </span>
          {{ 3 + (star ? 1 : 0) }}
        </span>
        <span class="action" key="reply"> <IconMessage /> Reply </span>
      </template>
      <template #avatar>
        <a-avatar>
          <img
            alt="avatar"
            src="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp"
          />
        </a-avatar>
      </template>
    </a-comment>
  </div>
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

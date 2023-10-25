<template>
  <div id="problem_details_comments_view">
    <a-row
      style="
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        margin-top: 30px;
        margin-bottom: 30px;
      "
    >
      <a-col :span="9" :offset="2">
        <a-input
          placeholder="请输入要搜索的评论内容"
          search-button
          style="width: 99%"
          v-model="searchText"
          @press-enter="getCommentList"
        >
          <template #button-icon>
            <icon-search />
          </template>
        </a-input>
      </a-col>
    </a-row>

    <a-comment
      v-for="(item, index) of commentData.commentTable"
      :key="index"
      :datetime="
        moment(
          moment(item.createTime).format('YYYY-MM-DD HH:mm:ss'),
          'YYYY-MM-DD HH:mm:ss'
        ).fromNow()
      "
      :author="item.userVO.userName || '用户'"
    >
      <template #content>
        <div v-if="curCommentEdit !== index">{{ item.content }}</div>
        <a-textarea v-else v-model="item.content" />
      </template>
      <template #actions>
        <span
          class="action"
          key="heart"
          @click="handleLikeClick(item)"
          style="cursor: pointer"
        >
          <span v-if="item.isLiked">
            <IconHeartFill :style="{ color: '#f53f3f' }" />
          </span>
          <span v-else>
            <IconHeart />
          </span>
          {{ item.thumbNum }}
        </span>
        <div
          v-if="
            curUser != undefined &&
            curUser?.userRole !== undefined &&
            curUser?.userRole !== null &&
            curUser?.userRole === roleEnum.ADMIN
          "
        >
          <a-button
            size="small"
            v-if="curCommentEdit !== index"
            style="margin-right: 10px; margin-left: auto; float: right"
            status="danger"
            type="primary"
            @click="handleDeleteComment(item.id)"
            >删除该评论
          </a-button>
          <a-button
            size="small"
            v-if="curCommentEdit !== index"
            style="margin-right: 10px; margin-left: auto; float: right"
            status="normal"
            type="primary"
            @click="curCommentEdit = index"
            >编辑该评论
          </a-button>
          <a-button
            size="small"
            v-if="curCommentEdit === index"
            style="margin-right: 10px; margin-left: auto; float: right"
            status="normal"
            type="primary"
            @click="handleEditSubmit(item)"
            >保存编辑
          </a-button>
        </div>
      </template>
      <template #avatar>
        <a-avatar
          :size="60"
          :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
          v-if="item.userVO.id === -1"
        >
          未登录
        </a-avatar>
        <a-avatar
          :size="60"
          :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
          v-else-if="
            item.userVO.userAvatar === undefined ||
            item.userVO.userAvatar === null
          "
        >
          {{ item.userVO.userName || "用户" }}
        </a-avatar>
        <a-avatar v-else :size="60" :style="{ marginLeft: '20px' }">
          <img
            alt="avatar"
            :src="
              'http://127.0.0.1:8102/api/avatars/' +
              item.userVO.id +
              '/' +
              item.userVO.userAvatar
            "
          />
        </a-avatar>
      </template>
      <a-divider />
    </a-comment>
    <a-pagination
      :total="commentData.commentCount"
      show-total
      show-jumper
      show-page-size
      :page-size-options="pageSizes"
      @page-size-change="handleSizeChange"
      @change="handleCurrentChange"
      v-model:current="curPage"
      v-model:page-size="pageSize"
      style="
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        margin: 2% auto;
        padding: 0;
        justify-content: center;
        -webkit-justify-content: center;
      "
    >
      <template #page-item="{ page }"> - {{ page }} -</template>
      <template #page-item-step="{ type }">
        <icon-send
          :style="
            type === 'previous' ? { transform: `rotate(180deg)` } : undefined
          "
        />
      </template>
      <template #page-item-ellipsis>
        <icon-sun-fill />
      </template>
    </a-pagination>
    <a-comment align="right">
      <template #avatar>
        <a-avatar
          :size="60"
          :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
          v-if="curUser.id === -1"
        >
          未登录
        </a-avatar>
        <a-avatar
          :size="60"
          :style="{ backgroundColor: '#3370ff', marginLeft: '20px' }"
          v-else-if="
            curUser.userAvatar === undefined || curUser.userAvatar === null
          "
        >
          {{ curUser.userName || "用户" }}
        </a-avatar>
        <a-avatar v-else :style="{ marginLeft: '20px' }">
          <img
            alt="avatar"
            :src="
              'http://127.0.0.1:8102/api/avatars/' +
              curUser.id +
              '/' +
              curUser.userAvatar
            "
          />
        </a-avatar>
      </template>
      <template #actions>
        <a-button
          key="1"
          type="primary"
          style="margin-top: 14px; margin-right: 30px"
          @click="handleCommentSubmit"
        >
          评论
        </a-button>
      </template>
      <template #content>
        <a-textarea
          placeholder="评论?"
          allow-clear
          style="white-space: pre-wrap; word-break: break-all"
          v-model="commentInput"
        />
      </template>
    </a-comment>
  </div>
</template>

<script setup lang="ts">
import {
  defineProps,
  inject,
  onMounted,
  reactive,
  ref,
  withDefaults,
} from "vue";
import moment from "moment";
import {
  IconSearch,
  IconSend,
  IconSunFill,
  IconHeart,
  IconHeartFill,
} from "@arco-design/web-vue/es/icon";
import { useRoute, useRouter } from "vue-router";
import store from "@/store";
import { CommentControllerService, ProblemControllerService } from "@/api";
import { Message } from "@arco-design/web-vue";
import { roleEnum } from "@/components/scripts/access/roleEnum";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

const route = useRoute();
const router = useRouter();
const curUser = store.state.user?.userInfo;

const searchText = ref("");
const commentInput = ref("");
const curCommentEdit = ref(-1);

const handleDeleteComment = async (id: number) => {
  let res = await CommentControllerService.deleteCommentUsingPost({
    id: id,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  } else {
    Message.success("删除成功");
    getCommentList();
  }
};

const handleCommentSubmit = async () => {
  if (commentInput.value === null || commentInput.value === "") {
    Message.info("输入内容不得为空");
    return;
  }
  let res = await CommentControllerService.addCommentUsingPost({
    problemId: parseInt(props.id),
    content: commentInput.value,
  });
  if (res.code == 0) {
    Message.error(res.message);
  } else {
    Message.info("评论成功");
    getCommentList();
  }
};

let curPage = ref(1);
let pageSize = ref(8);
let pageSizes = ref([8, 16, 24]);
const commentData = reactive({
  commentTable: [],
  commentCount: 0,
});

const handleEditSubmit = async (item: any) => {
  let res = await CommentControllerService.editCommentUsingPost({
    id: item.id || -1,
    content: item.content || "",
    problemId: item.problemId || -1,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  curCommentEdit.value = -1;
};

const getCommentList = async () => {
  let res = await CommentControllerService.listCommentVoByPageUsingPost({
    current: curPage.value,
    pageSize: pageSize.value,
    problemId: parseInt(props.id),
    searchText: searchText.value,
  });
  if (res.code !== 1) {
    Message.error("err" + res.message);
    return;
  }
  curCommentEdit.value = -1;
  commentData.commentTable = res.data.records || [];
  commentData.commentCount = parseInt(res.data.total);
  if (Math.ceil(commentData.commentCount / pageSize.value) < curPage.value) {
    curPage.value = Math.ceil(commentData.commentCount / pageSize.value);
  }
};

const handleSizeChange = (val: number) => {
  pageSize.value = val;
  getCommentList();
};
const handleCurrentChange = (val: number) => {
  curPage.value = val;
  getCommentList();
};
const handleLikeClick = async (comment: any) => {
  if (curUser == null || curUser.id < 0) {
    Message.info("你需要先登录");
    return;
  }
  comment.isLiked = !comment.isLiked;
  const res = await CommentControllerService.doLikeCommentUsingPost({
    commentId: comment.id,
  });
  if (res.code == 0) {
    Message.error("err" + res.message);
  } else {
    if (comment.isLiked) {
      (comment.thumbNum as number)++;
    } else {
      (comment.thumbNum as number)--;
    }
  }
};
onMounted(() => {
  getCommentList();
});

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

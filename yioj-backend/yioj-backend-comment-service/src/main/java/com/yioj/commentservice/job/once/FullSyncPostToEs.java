package com.yioj.commentservice.job.once;

//import com.yi.oj.esdao.CommentEsDao;

import com.yioj.commentservice.esdao.CommentEsDao;
import com.yioj.commentservice.service.CommentService;
import com.yioj.model.model.dto.comment.CommentEsDTO;
import com.yioj.model.model.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全量同步帖子到 es
 *
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private CommentService commentService;

    @Resource
    private CommentEsDao commentEsDao;

    @Override
    public void run(String... args) {
        List<Comment> commentList = commentService.list();
        if (CollectionUtils.isEmpty(commentList)) {
            return;
        }
        List<CommentEsDTO> commentEsDTOList = commentList.stream().map(CommentEsDTO::objToDto).collect(Collectors.toList());
        final int pageSize = 500;
        int total = commentEsDTOList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            commentEsDao.saveAll(commentEsDTOList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }
}

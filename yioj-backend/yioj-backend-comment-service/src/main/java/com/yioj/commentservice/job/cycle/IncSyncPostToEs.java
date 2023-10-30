package com.yioj.commentservice.job.cycle;

//import com.yi.oj.esdao.CommentEsDao;

import com.yioj.commentservice.esdao.CommentEsDao;
import com.yioj.commentservice.mapper.CommentMapper;
import com.yioj.model.model.dto.comment.CommentEsDTO;
import com.yioj.model.model.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步帖子到 es
 *
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncPostToEs {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private CommentEsDao commentEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<Comment> commentList = commentMapper.listPostWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(commentList)) {
            log.info("no inc post");
            return;
        }
        List<CommentEsDTO> postEsDTOList = commentList.stream()
                .map(CommentEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = postEsDTOList.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            commentEsDao.saveAll(postEsDTOList.subList(i, end));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }
}

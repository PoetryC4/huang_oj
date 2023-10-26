package com.huang.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.oj.model.dto.comment.CommentQueryRequest;
import com.huang.oj.model.dto.submission.ProblemSubmitQuest;
import com.huang.oj.model.dto.submission.SubmissionQueryQuest;
import com.huang.oj.model.entity.Comment;
import com.huang.oj.model.entity.Comment;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.model.entity.User;
import com.huang.oj.model.vo.CommentVO;
import com.huang.oj.model.vo.SimpleSubmissionVO;
import com.huang.oj.model.vo.SubmissionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【submission(提交)】的数据库操作Service
* @createDate 2023-09-26 21:03:14
*/
public interface CommentService extends IService<Comment> {

    /**
     * 校验
     *
     * @param comment
     * @param add
     */
    void validComment(Comment comment, boolean add);

    /**
     * 获取查询条件
     *
     * @param commentQueryRequest
     * @return
     */
    QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param commentQueryRequest
     * @return
     */
    /*    Page<Comment> searchFromEs(CommentQueryRequest commentQueryRequest);*/

    /**
     * 获取帖子封装
     *
     * @param comment
     * @param request
     * @return
     */
    CommentVO getCommentVO(Comment comment, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);

    boolean doLikeComment(Long commentId, Long userId, HttpServletRequest request);
    Page<Comment> searchFromEs(CommentQueryRequest commentQueryRequest);
}

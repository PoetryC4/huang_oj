package com.yioj.commentservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yioj.model.model.dto.comment.CommentQueryRequest;
import com.yioj.model.model.entity.Comment;
import com.yioj.model.model.vo.CommentVO;

import javax.servlet.http.HttpServletRequest;

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

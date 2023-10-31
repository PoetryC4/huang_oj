package com.yioj.commentservice.controller;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yioj.clientservice.service.UserFeignClient;
import com.yioj.commentservice.service.CommentService;
import com.yioj.commentservice.service.CommentThumbService;
import com.yioj.common.common.BaseResponse;
import com.yioj.common.common.DeleteRequest;
import com.yioj.common.common.ErrorCode;
import com.yioj.common.common.ResultUtils;
import com.yioj.common.constant.UserConstant;
import com.yioj.common.exception.BusinessException;
import com.yioj.common.exception.ThrowUtils;
import com.yioj.model.model.dto.comment.*;
import com.yioj.model.model.entity.Comment;
import com.yioj.model.model.entity.User;
import com.yioj.model.model.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserFeignClient userFeignClient;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param commentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        if (commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        commentService.validComment(comment, true);
        User loginUser = userFeignClient.getLoginUser(request);
        comment.setUserId(loginUser.getId());
        comment.setThumbNum(0);
        boolean result = commentService.save(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newCommentId = comment.getId();
        return ResultUtils.success(newCommentId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldComment.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = commentService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param commentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest == null || commentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentUpdateRequest, comment);
        // 参数校验
        commentService.validComment(comment, false);
        long id = commentUpdateRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = commentService.updateById(comment);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CommentVO> getCommentVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = commentService.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(commentService.getCommentVO(comment, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CommentVO>> listCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
            HttpServletRequest request) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 24, ErrorCode.PARAMS_ERROR);
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CommentVO>> listMyCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
            HttpServletRequest request) {
        if (commentQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        commentQueryRequest.setUserId(loginUser.getId());
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    // endregion

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<CommentVO>> searchCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
            HttpServletRequest request) {
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Comment> commentPage = commentService.searchFromEs(commentQueryRequest);
        if(commentPage == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param commentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editComment(@RequestBody CommentEditRequest commentEditRequest, HttpServletRequest request) {
        if (commentEditRequest == null || commentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentEditRequest, comment);
        // 参数校验
        commentService.validComment(comment, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = commentEditRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldComment.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = commentService.updateById(comment);
        return ResultUtils.success(result);
    }
    
    @PostMapping("/like")
    public BaseResponse<Boolean> doLikeComment(@RequestBody CommentThumbUpdateRequest commentThumbUpdateRequest, HttpServletRequest request) {
        if (commentThumbUpdateRequest == null || commentThumbUpdateRequest.getCommentId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = commentService.getById(commentThumbUpdateRequest.getCommentId());
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        boolean result = commentService.doLikeComment(comment.getId(), user.getId(), request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }
}

package com.huang.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huang.oj.common.BaseResponse;
import com.huang.oj.common.DeleteRequest;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.common.ResultUtils;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.exception.ThrowUtils;
import com.huang.oj.model.dto.submission.ProblemSubmitQuest;
import com.huang.oj.model.dto.submission.SubmissionQueryQuest;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.model.entity.User;
import com.huang.oj.model.enums.SubmissionStatusEnum;
import com.huang.oj.model.vo.SubmissionVO;
import com.huang.oj.service.SubmissionService;
import com.huang.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/submission")
@Slf4j
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param problemSubmitQuest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/add")
    public BaseResponse<Long> doSubmit(@RequestBody ProblemSubmitQuest problemSubmitQuest,
                                         HttpServletRequest request) {
        if (problemSubmitQuest == null || problemSubmitQuest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);

        submissionService.ValidSubmission(problemSubmitQuest, loginUser);

        Submission submission = new Submission();
        submission.setCode(problemSubmitQuest.getCode());
        submission.setProblemId(problemSubmitQuest.getProblemId());
        submission.setLanguage(problemSubmitQuest.getLanguage());
        submission.setUserId(loginUser.getId());
        submission.setJudgeStatus(SubmissionStatusEnum.COMPILING.getValue());
        boolean save = submissionService.save(submission);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(submission.getId());
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSubmission(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Submission submission = submissionService.getById(id);
        ThrowUtils.throwIf(submission == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!submission.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = submissionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<SubmissionVO> getSubmissionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Submission submission = submissionService.getById(id);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(submissionService.getSubmissionVO(submission, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param submissionQueryQuest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SubmissionVO>> listSubmissionVOByPage(@RequestBody SubmissionQueryQuest submissionQueryQuest,
                                                                HttpServletRequest request) {
        long current = submissionQueryQuest.getCurrent();
        long size = submissionQueryQuest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Submission> submissionPage = submissionService.page(new Page<>(current, size),
                submissionService.getQueryWrapper(submissionQueryQuest));
        return ResultUtils.success(submissionService.getSubmissionVOPage(submissionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param submissionQueryQuest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SubmissionVO>> listMySubmissionVOByPage(@RequestBody SubmissionQueryQuest submissionQueryQuest,
                                                               HttpServletRequest request) {
        if (submissionQueryQuest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = submissionQueryQuest.getCurrent();
        long size = submissionQueryQuest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Submission> submissionPage = submissionService.page(new Page<>(current, size),
                submissionService.getQueryWrapper(submissionQueryQuest));
        return ResultUtils.success(submissionService.getSubmissionVOPage(submissionPage, request));
    }


}

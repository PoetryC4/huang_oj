package com.yioj.submissionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yioj.clientservice.service.UserFeignClient;
import com.yioj.common.common.BaseResponse;
import com.yioj.common.common.DeleteRequest;
import com.yioj.common.common.ErrorCode;
import com.yioj.common.common.ResultUtils;
import com.yioj.common.constant.UserConstant;
import com.yioj.common.exception.BusinessException;
import com.yioj.common.exception.ThrowUtils;
import com.yioj.common.utils.IpRateLimiter;
import com.yioj.common.utils.RedisUtils;
import com.yioj.model.model.dto.submission.ProblemSubmitQuest;
import com.yioj.model.model.dto.submission.ProblemTestExampleRequest;
import com.yioj.model.model.dto.submission.SimpleSubmissionQueryQuest;
import com.yioj.model.model.dto.submission.SubmissionQueryQuest;
import com.yioj.model.model.entity.Submission;
import com.yioj.model.model.entity.User;
import com.yioj.model.model.enums.SubmissionResultEnum;
import com.yioj.model.model.judge.JudgeResult;
import com.yioj.model.model.vo.SimpleSubmissionVO;
import com.yioj.model.model.vo.SubmissionVO;
import com.yioj.model.model.vo.UserVO;
import com.yioj.submissionservice.judge.JudgeService;
import com.yioj.submissionservice.service.SubmissionService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子点赞接口
 */
@RestController
@RequestMapping("/")
@Slf4j
public class SubmissionController {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private JudgeService judgeService;

    private static IpRateLimiter ipRateLimiter;

    // 每秒每个IP10个请求
    private static final double submitRequestsPerSecond = 10;

    static {
        ipRateLimiter = new IpRateLimiter(submitRequestsPerSecond);
    }

    /**
     * 点赞 / 取消点赞
     *
     * @param problemSubmitQuest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/submit/add")
    public BaseResponse<SubmissionVO> doSubmit(@RequestBody ProblemSubmitQuest problemSubmitQuest,
                                               HttpServletRequest request) {
        String clientIp = request.getRemoteAddr(); // 获取客户端IP地址
        boolean allowed = ipRateLimiter.allowRequest(clientIp);
        if (!allowed) {
            // 请求限制，返回相应的错误信息或状态码
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
        if (problemSubmitQuest == null || problemSubmitQuest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        final User loginUser = userFeignClient.getLoginUser(request);

        submissionService.ValidSubmission(problemSubmitQuest, loginUser);

        Submission submission = new Submission();
        submission.setCode(problemSubmitQuest.getCode());
        submission.setProblemId(problemSubmitQuest.getProblemId());
        submission.setLanguage(problemSubmitQuest.getLanguage());
        submission.setUserId(loginUser.getId());
        submission.setJudgeStatus(SubmissionResultEnum.COMPILING.getValue());
        boolean save = submissionService.save(submission);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);

        JudgeResult judgeResult;
        // 限流异常捕捉
        try {
            judgeResult = judgeService.doJudge(submission.getId());
        } catch (RequestNotPermitted e) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }

        SubmissionVO submissionVO = SubmissionVO.objToVo(submission);
        UserVO loginUserVO = new UserVO();
        BeanUtils.copyProperties(loginUser, loginUserVO);
        submissionVO.setJudgeResult(judgeResult);
        submissionVO.setUserVO(loginUserVO);

        return ResultUtils.success(submissionVO);
    }

    @PostMapping("/submit/test")
    public BaseResponse<JudgeResult> testSubmit(@RequestBody ProblemTestExampleRequest problemTestExampleRequest,
                                                HttpServletRequest request) {
        ProblemSubmitQuest problemSubmitQuest = problemTestExampleRequest.getProblemSubmitQuest();
        if (problemSubmitQuest == null || problemSubmitQuest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);

        submissionService.ValidSubmission(problemSubmitQuest, loginUser);

        JudgeResult judgeResult;
        // 限流异常捕捉
        try {
            judgeResult = judgeService.testJudge(problemTestExampleRequest);
        } catch (RequestNotPermitted e) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }

        return ResultUtils.success(judgeResult);
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
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Submission submission = submissionService.getById(id);
        ThrowUtils.throwIf(submission == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!submission.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
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
        String key = "SubmissionVO:" + id;
        Submission submission = (Submission) redisUtils.get(key);
        if (submission == null) {
            submission = submissionService.getById(id);
            if (submission == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }
            redisUtils.set(key, submission);
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
        if (submissionQueryQuest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = submissionQueryQuest.getCurrent();
        long size = submissionQueryQuest.getPageSize();
        submissionQueryQuest.setUserId(null);
        // 限制爬虫
        ThrowUtils.throwIf(size > 25, ErrorCode.PARAMS_ERROR);
        Page<Submission> submissionPage = submissionService.page(new Page<>(current, size),
                submissionService.getQueryWrapper(submissionQueryQuest));
        return ResultUtils.success(submissionService.getSubmissionVOPage(submissionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param simpleSubmissionQueryQuest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<SimpleSubmissionVO>> listMySubmissionVOByPage(@RequestBody SimpleSubmissionQueryQuest simpleSubmissionQueryQuest,
                                                                           HttpServletRequest request) {
        if (simpleSubmissionQueryQuest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        if (loginUser == null || loginUser.getId() < 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        simpleSubmissionQueryQuest.setUserId(loginUser.getId());
        String title = simpleSubmissionQueryQuest.getTitle();
        Integer judgeStatus = simpleSubmissionQueryQuest.getJudgeStatus();
        long current = simpleSubmissionQueryQuest.getCurrent();
        long size = simpleSubmissionQueryQuest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 25, ErrorCode.PARAMS_ERROR);
        Page<SimpleSubmissionVO> simpleSubmissionVOPage = new Page<>();
        List<SimpleSubmissionVO> submissionVOList = submissionService.getSimpleSubmissionPage(current, size, title, judgeStatus, request);
        simpleSubmissionVOPage.setRecords(submissionVOList);
        simpleSubmissionVOPage.setTotal(submissionService.getSimpleSubmissionCount(current, size, title, judgeStatus, request));
        return ResultUtils.success(simpleSubmissionVOPage);
    }

}

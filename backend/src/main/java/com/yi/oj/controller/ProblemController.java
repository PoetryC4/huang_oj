package com.yi.oj.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yi.oj.annotation.AuthCheck;
import com.yi.oj.common.BaseResponse;
import com.yi.oj.common.DeleteRequest;
import com.yi.oj.common.ErrorCode;
import com.yi.oj.common.ResultUtils;
import com.yi.oj.constant.UserConstant;
import com.yi.oj.exception.BusinessException;
import com.yi.oj.exception.ThrowUtils;
import com.yi.oj.model.dto.problem.*;
import com.yi.oj.model.entity.Problem;
import com.yi.oj.model.entity.User;
import com.yi.oj.model.vo.ProblemVO;
import com.yi.oj.service.ProblemService;
import com.yi.oj.service.UserService;
import com.yi.oj.utils.RedisUtils;
import com.yi.oj.model.dto.problem.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 
 
 */
@RestController
@RequestMapping("/problem")
@Slf4j
public class ProblemController {

    @Value("${spring.redis.timeout}")
    private Integer redisTimeout;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ProblemService problemService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param problemAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpServletRequest request) {
        if (problemAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemAddRequest, problem);
        JudgeConfig judgeConfig1 = problemAddRequest.getJudgeConfig();
        if (judgeConfig1 != null) {
            problem.setJudgeConfig(JSON.toJSONString(judgeConfig1));
        }
        List<String> problemTag1 = problemAddRequest.getTags();
        if (problemTag1 != null) {
            problem.setTags(JSON.toJSONString(problemTag1));
        }
        FunctionConfig functionConfig1 = problemAddRequest.getFunctionConfig();
        if (functionConfig1 != null) {
            problem.setFunctionConfig(com.alibaba.fastjson2.JSON.toJSONString(functionConfig1));
        }
        JudgeCases judgeCases1 = problemAddRequest.getJudgeCases();
        if (judgeCases1 != null) {
            problem.setJudgeCases(com.alibaba.fastjson2.JSON.toJSONString(judgeCases1));
        }
        problemService.validProblem(problem, true);
        User loginUser = userService.getLoginUser(request);
        problem.setUserId(loginUser.getId());
        problem.setDisLikeNum(0);
        problem.setThumbNum(0);
        boolean result = problemService.save(problem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newProblemId = problem.getId();
        return ResultUtils.success(newProblemId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProblem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Problem problem = problemService.getById(id);
        ThrowUtils.throwIf(problem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!problem.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = problemService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param problemUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateProblem(@RequestBody ProblemUpdateRequest problemUpdateRequest) {
        if (problemUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemUpdateRequest, problem);
        JudgeConfig judgeConfig1 = problemUpdateRequest.getJudgeConfig();
        if (judgeConfig1 != null) {
            problem.setJudgeConfig(JSON.toJSONString(judgeConfig1));
        }
        List<String> problemTag1 = problemUpdateRequest.getTags();
        if (problemTag1 != null) {
            problem.setTags(JSON.toJSONString(problemTag1));
        }
        FunctionConfig functionConfig1 = problemUpdateRequest.getFunctionConfig();
        if (functionConfig1 != null) {
            problem.setFunctionConfig(com.alibaba.fastjson2.JSON.toJSONString(functionConfig1));
        }
        JudgeCases judgeCases1 = problemUpdateRequest.getJudgeCases();
        if (judgeCases1 != null) {
            problem.setJudgeCases(com.alibaba.fastjson2.JSON.toJSONString(judgeCases1));
        }
        // 参数校验
        problemService.validProblem(problem, false);
        long id = problemUpdateRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ProblemVO> getProblemVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }/*
        String key = "ProblemVO:"+id;
        Problem problem = (Problem) redisUtils.get(key);
        if(problem == null) {
            problem = problemService.getById(id);
            if (problem == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }
            redisUtils.set(key, problem, redisTimeout);
        }*/
        return ResultUtils.success(problemService.getProblemVO(problem, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param problemQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ProblemVO>> listProblemVOByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
                                                             HttpServletRequest request) {
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        Integer difficulty = problemQueryRequest.getDifficulty();
        Integer status = problemQueryRequest.getStatus();
        String title = problemQueryRequest.getTitle();
        // 限制爬虫
        ThrowUtils.throwIf(size > 25, ErrorCode.PARAMS_ERROR);
        //Page<Problem> problemPage = problemService.page(new Page<>(current, size),
        //        problemService.getQueryWrapper(problemQueryRequest));
        Page<Problem> problemPage = new Page<>();
        List<Problem> problemRes = problemService.getProblemQueryRes(current, size, title, difficulty, status, request);
        problemPage.setRecords(problemRes);
        problemPage.setTotal(problemService.getProblemQueryCount(current, size, title, difficulty, status, request));
        return ResultUtils.success(problemService.getProblemVOPage(problemPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param problemUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> editProblem(@RequestBody ProblemUpdateRequest problemUpdateRequest, HttpServletRequest request) {
        if (problemUpdateRequest == null || problemUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemUpdateRequest, problem);
        List<String> tags = problemUpdateRequest.getTags();
        if (tags != null) {
            problem.setTags(GSON.toJson(tags));
        }
        // 参数校验
        problemService.validProblem(problem, false);
        User loginUser = userService.getLoginUser(request);
        long id = problemUpdateRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldProblem.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }

    @PostMapping("/like")
    public BaseResponse<Boolean> doLikeProblem(@RequestBody ProblemThumbUpdateRequest problemThumbUpdateRequest, HttpServletRequest request) {
        if (problemThumbUpdateRequest == null || problemThumbUpdateRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getById(problemThumbUpdateRequest.getProblemId());
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = userService.getLoginUser(request);
        boolean result = problemService.doLikeProblem(problem.getId(), user.getId(), request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/dislike")
    public BaseResponse<Boolean> doDislikeProblem(@RequestBody ProblemDislikeUpdateRequest problemDislikeUpdateRequest, HttpServletRequest request) {
        if (problemDislikeUpdateRequest == null || problemDislikeUpdateRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getById(problemDislikeUpdateRequest.getProblemId());
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = userService.getLoginUser(request);
        boolean result = problemService.doDislikeProblem(problem.getId(), user.getId(), request);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }
}

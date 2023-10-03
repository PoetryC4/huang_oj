package com.huang.oj.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.huang.oj.annotation.AuthCheck;
import com.huang.oj.common.BaseResponse;
import com.huang.oj.common.DeleteRequest;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.common.ResultUtils;
import com.huang.oj.constant.UserConstant;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.exception.ThrowUtils;
import com.huang.oj.model.dto.post.PostAddRequest;
import com.huang.oj.model.dto.post.PostEditRequest;
import com.huang.oj.model.dto.post.PostUpdateRequest;
import com.huang.oj.model.dto.problem.*;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.User;
import com.huang.oj.model.vo.ProblemVO;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.huang.oj.utils.SqlUtils.isAnyNull;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/problem")
@Slf4j
public class ProblemController {

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
        ProblemTag problemTag1 = problemAddRequest.getTags();
        if (problemTag1 != null) {
            problem.setTags(JSON.toJSONString(problemTag1));
        }
        List<JudgeCase> judgeCases1 = problemAddRequest.getJudgeCase();
        if (judgeCases1 != null) {
            problem.setJudgeCase(com.alibaba.fastjson2.JSON.toJSONString(judgeCases1));
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
        ProblemTag problemTag1 = problemUpdateRequest.getTags();
        if (problemTag1 != null) {
            problem.setTags(JSON.toJSONString(problemTag1));
        }
        List<JudgeCase> judgeCases1 = problemUpdateRequest.getJudgeCase();
        if (judgeCases1 != null) {
            problem.setJudgeCase(com.alibaba.fastjson2.JSON.toJSONString(judgeCases1));
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
        }
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
        // 限制爬虫
        ThrowUtils.throwIf(size > 25, ErrorCode.PARAMS_ERROR);
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
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
        ProblemTag tags = problemUpdateRequest.getTags();
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

}

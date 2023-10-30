package com.yi.oj.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yi.oj.common.ErrorCode;
import com.yi.oj.constant.CommonConstant;
import com.yi.oj.constant.UserConstant;
import com.yi.oj.exception.BusinessException;
import com.yi.oj.judge.sandbox.model.JudgeResult;
import com.yi.oj.mapper.SubmissionMapper;
import com.yi.oj.model.dto.submission.ProblemSubmitQuest;
import com.yi.oj.model.dto.submission.SubmissionQueryQuest;
import com.yi.oj.model.entity.Problem;
import com.yi.oj.model.entity.Submission;
import com.yi.oj.model.entity.User;
import com.yi.oj.model.enums.ProblemLanguageEnum;
import com.yi.oj.model.enums.SubmissionResultEnum;
import com.yi.oj.model.vo.*;
import com.yi.oj.model.vo.SimpleSubmission;
import com.yi.oj.model.vo.SimpleSubmissionVO;
import com.yi.oj.model.vo.SubmissionVO;
import com.yi.oj.model.vo.UserVO;
import com.yi.oj.service.ProblemService;
import com.yi.oj.service.SubmissionService;
import com.yi.oj.service.UserService;
import com.yi.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【submission(提交)】的数据库操作Service实现
 * @createDate 2023-09-26 21:03:14
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission>
        implements SubmissionService {

    @Resource
    private SubmissionMapper submissionMapper;
    @Resource
    private ProblemService problemService;
    @Resource
    private UserService userService;

    /**
     * 点赞
     *
     * @param problemSubmitQuest
     * @param loginUser
     * @return
     */
    @Override
    public void ValidSubmission(ProblemSubmitQuest problemSubmitQuest, User loginUser) {
        Long problemId = problemSubmitQuest.getProblemId();
        String language = problemSubmitQuest.getLanguage();
        if (ProblemLanguageEnum.getEnumByValue(language) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编译语言选择出错");
        }
        // 判断实体是否存在，根据类别获取实体
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
    }

    /**
     * 获取查询包装类
     *
     * @param submissionQueryQuest
     * @return
     */
    @Override
    public QueryWrapper<Submission> getQueryWrapper(SubmissionQueryQuest submissionQueryQuest) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        if (submissionQueryQuest == null) {
            return queryWrapper;
        }
        String sortField = submissionQueryQuest.getSortField();
        String sortOrder = submissionQueryQuest.getSortOrder();
        String language = submissionQueryQuest.getLanguage();
        if (StringUtils.isNotBlank(language) && ProblemLanguageEnum.getEnumByValue(language) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编译语言选择出错");
        }
        Integer judgeStatus = submissionQueryQuest.getJudgeStatus();
        Long problemId = submissionQueryQuest.getProblemId();
        Long userId = submissionQueryQuest.getUserId();

        if (StringUtils.isNotBlank(language)) {
            queryWrapper.eq("language", language);
        }
        if (problemId != null && problemId > 0) {
            queryWrapper.eq("problemId", problemId);
        }
        if (userId != null && userId > 0) {
            queryWrapper.eq("userId", userId);
        }
        if (judgeStatus != null && SubmissionResultEnum.getEnumByValue(judgeStatus) != null) {
            queryWrapper.eq("judgeStatus", judgeStatus);
        }
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        queryWrapper.orderByDesc("submitTime");
        return queryWrapper;
    }

    @Override
    public SubmissionVO getSubmissionVO(Submission submission, HttpServletRequest request) {
        SubmissionVO submissionVO = SubmissionVO.objToVo(submission);

        User loginUser = userService.getLoginUser(request);

        Long problemId = submissionVO.getProblemId();
        Problem problem = null;
        if (problemId != null && problemId > 0) {
            problem = problemService.getById(problemId);
        }
        Long userId = submission.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        submissionVO.setUserVO(userVO);

        if (!Objects.equals(userId, submission.getUserId()) || !userService.isAdmin(loginUser)) {
            submissionVO.setCode(null);
        }
        return submissionVO;
    }

    @Override
    public Page<SubmissionVO> getSubmissionVOPage(Page<Submission> submissionPage, HttpServletRequest request) {
        List<Submission> problemList = submissionPage.getRecords();
        Page<SubmissionVO> problemVOPage = new Page<>(submissionPage.getCurrent(), submissionPage.getSize(), submissionPage.getTotal());
        if (CollectionUtils.isEmpty(problemList)) {
            return problemVOPage;
        }
        // 填充信息
        List<SubmissionVO> problemVOList = problemList.stream().map(submission -> {
            SubmissionVO submissionVO = SubmissionVO.objToVo(submission);

            Long problemId = submissionVO.getProblemId();
            Problem problem = null;
            if (problemId != null && problemId > 0) {
                problem = problemService.getById(problemId);
            }
            Long userId = submission.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                user = userService.getById(userId);
            }
            UserVO userVO = userService.getUserVO(user);
            submissionVO.setUserVO(userVO);

            return submissionVO;
        }).collect(Collectors.toList());
        problemVOPage.setRecords(problemVOList);
        return problemVOPage;
    }

    @Override
    public List<SimpleSubmissionVO> getSimpleSubmissionPage(long current, long size, String title, Integer judgeStatus, HttpServletRequest request) {
        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
        }
        Long id = loginUser.getId();
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long offset = Math.max(0, (current - 1)) * size;
        List<SimpleSubmission> simpleSubmissionRes = submissionMapper.getSimpleSubmissionRes(size, offset, title, judgeStatus, id);
        List<SimpleSubmissionVO> simpleSubmissionVOList = simpleSubmissionRes.stream().map(simpleSubmission -> {
            Long id1 = simpleSubmission.getId();
            Long problemId = simpleSubmission.getProblemId();
            Long userId = simpleSubmission.getUserId();
            String title1 = simpleSubmission.getTitle();
            String judgeResult = simpleSubmission.getJudgeResult();
            Date submitTime = simpleSubmission.getSubmitTime();

            SimpleSubmissionVO simpleSubmissionVO = new SimpleSubmissionVO();
            simpleSubmissionVO.setId(id1);
            simpleSubmissionVO.setProblemId(problemId);
            simpleSubmissionVO.setUserId(userId);
            simpleSubmissionVO.setTitle(title1);
            simpleSubmissionVO.setJudgeResult(JSON.parseObject(judgeResult, JudgeResult.class));
            simpleSubmissionVO.setSubmitTime(submitTime);

            return simpleSubmissionVO;
        }).collect(Collectors.toList());
        return simpleSubmissionVOList;
    }

    @Override
    public Integer getSimpleSubmissionCount(long current, long size, String title, Integer judgeStatus, HttpServletRequest request) {
        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
        }
        Long id = loginUser.getId();
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long offset = Math.max(0, (current - 1)) * size;
        return submissionMapper.getSimpleSubmissionCount(size, offset, title, judgeStatus, id);
    }

}





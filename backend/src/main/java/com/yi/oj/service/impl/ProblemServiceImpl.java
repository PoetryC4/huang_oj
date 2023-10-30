package com.yi.oj.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.yi.oj.common.ErrorCode;
import com.yi.oj.constant.CommonConstant;
import com.yi.oj.constant.UserConstant;
import com.yi.oj.exception.BusinessException;
import com.yi.oj.exception.ThrowUtils;
import com.yi.oj.mapper.ProblemDislikeMapper;
import com.yi.oj.mapper.ProblemMapper;
import com.yi.oj.mapper.ProblemThumbMapper;
import com.yi.oj.mapper.SubmissionMapper;
import com.yi.oj.model.dto.problem.FunctionConfig;
import com.yi.oj.model.dto.problem.JudgeCases;
import com.yi.oj.model.dto.problem.JudgeConfig;
import com.yi.oj.model.dto.problem.ProblemQueryRequest;
import com.yi.oj.model.entity.*;
import com.yi.oj.model.entity.*;
import com.yi.oj.model.enums.SubmissionResultEnum;
import com.yi.oj.model.vo.ProblemVO;
import com.yi.oj.model.vo.UserVO;
import com.yi.oj.service.ProblemDislikeService;
import com.yi.oj.service.ProblemService;
import com.yi.oj.service.ProblemThumbService;
import com.yi.oj.service.UserService;
import com.yi.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【problem(题)】的数据库操作Service实现
 * @createDate 2023-09-27 16:06:56
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem>
        implements ProblemService {

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    @Resource
    private ProblemMapper problemMapper;

    @Resource
    private SubmissionMapper submissionMapper;

    @Resource
    private ProblemThumbMapper problemThumbMapper;

    @Resource
    private ProblemDislikeMapper problemDislikeMapper;

    @Resource
    private ProblemThumbService problemThumbService;

    @Resource
    private ProblemDislikeService problemDislikeService;


    @Override
    public void validProblem(Problem problem, boolean add) {
        if (problem == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = problem.getTitle();
        String content = problem.getContent();
        String solution = problem.getSolution();
        String tags = problem.getTags();
        JudgeConfig judgeConfig = JSONObject.parseObject(problem.getJudgeConfig(), JudgeConfig.class);
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags) || SqlUtils.isAnyNull(judgeConfig, judgeConfig.getMemoryLimit(), judgeConfig.getStackLimit(), judgeConfig.getTimeLimit()), ErrorCode.PARAMS_ERROR);
        }
        //有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(solution) && solution.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param problemQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        if (problemQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = problemQueryRequest.getSortField();
        String sortOrder = problemQueryRequest.getSortOrder();
        String title = problemQueryRequest.getTitle();

        queryWrapper.eq("isDelete", false);
        queryWrapper.and(wrapper -> wrapper.eq(StringUtils.isNotBlank(title), "id", title).or().like(StringUtils.isNotBlank(title), "title", title).and(wrapper1 -> wrapper1.eq("isDelete", false)));
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ProblemVO getProblemVO(Problem problem, HttpServletRequest request) {
        ProblemVO problemVO = ProblemVO.objToVo(problem);
        Long userId = problem.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        problemVO.setUserVO(userVO);

        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
            if (!userService.isAdmin(loginUser)) {
                JudgeCases judgeCases = problemVO.getJudgeCases();
                judgeCases.setExpected(null);
                FunctionConfig functionConfig = problemVO.getFunctionConfig();
                functionConfig.setValidCode(null);
                functionConfig.setInitCode(null);
                functionConfig.setCorrectCode(null);

                problemVO.setFunctionConfig(functionConfig);
                String[] substrings = judgeCases.getInput().split("\n");

                StringBuilder cutInputCases = new StringBuilder();
                int count = problemVO.getJudgeConfig().getTestCaseProvided() * problemVO.getFunctionConfig().getVarCount();
                for (int i = 0; i < count && i < substrings.length; i++) {
                    cutInputCases.append(substrings[i]);
                    if (i < count - 1) {
                        cutInputCases.append("\n");
                    }
                }
                judgeCases.setInput(cutInputCases.toString());
            }
        }
        if (loginUser == null || loginUser.getId() == null) {
            problemVO.setIsSolved(0);
            problemVO.setIsLiked(false);
            problemVO.setIsDisliked(false);
        } else {
            QueryWrapper<ProblemDislike> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("problemId",problemVO.getId());
            queryWrapper3.eq("userId",loginUser.getId());
            if(problemDislikeMapper.selectCount(queryWrapper3) >0) {
                problemVO.setIsDisliked(true);
            } else {
                problemVO.setIsDisliked(false);
            }
            QueryWrapper<ProblemThumb> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("problemId",problemVO.getId());
            queryWrapper2.eq("userId",loginUser.getId());
            if(problemThumbMapper.selectCount(queryWrapper2) >0) {
                problemVO.setIsLiked(true);
            } else {
                problemVO.setIsLiked(false);
            }
            QueryWrapper<Submission> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("userId", loginUser.getId());
            queryWrapper1.eq("problemId", problemVO.getId());
            long count1 = submissionMapper.selectCount(queryWrapper1);
            queryWrapper1.eq("judgeStatus", SubmissionResultEnum.ACCEPT.getValue());
            long count2 = submissionMapper.selectCount(queryWrapper1);
            if (count2 > 0) {
                problemVO.setIsSolved(1);
            } else if (count1 > 0) {
                problemVO.setIsSolved(-1);
            } else {
                problemVO.setIsSolved(0);
            }
        }
        Long problemId = problem.getId();
        Long submitData = problemMapper.getSubmitTimes(problemId);
        Long submitAc = problemMapper.getSubmitAccepted(problemId);
        problemVO.setAccpetedCount(submitAc);
        problemVO.setSubmittedCount(submitData);
        if (Objects.equals(.0, Double.parseDouble(submitData + ""))) {
            problemVO.setAcceptance(.0);
        } else {
            problemVO.setAcceptance(Double.parseDouble(submitAc + "") / Double.parseDouble(submitData + ""));
        }

        return problemVO;
    }

    @Override
    public Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request) {
        List<Problem> problemList = problemPage.getRecords();
        Page<ProblemVO> problemVOPage = new Page<>(problemPage.getCurrent(), problemPage.getSize(), problemPage.getTotal());
        if (CollectionUtils.isEmpty(problemList)) {
            return problemVOPage;
        }
        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
        }

        final boolean isAdmin = userService.isAdmin(loginUser);
        final boolean isLoggedin = loginUser == null || loginUser.getId() == null;
        // 填充信息
        User finalLoginUser = loginUser;
        List<ProblemVO> problemVOList = problemList.stream().map(problem -> {
            ProblemVO problemVO = ProblemVO.objToVo(problem);

            Long userId = problem.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                user = userService.getById(userId);
            }
            UserVO userVO = userService.getUserVO(user);
            problemVO.setUserVO(userVO);

            if (isLoggedin) {
                problemVO.setIsSolved(0);
                problemVO.setIsLiked(false);
                problemVO.setIsDisliked(false);
                if (!isAdmin) {
                    JudgeCases judgeCases = problemVO.getJudgeCases();
                    judgeCases.setExpected(null);
                    FunctionConfig functionConfig = problemVO.getFunctionConfig();
                    functionConfig.setValidCode(null);
                    functionConfig.setInitCode(null);
                    functionConfig.setCorrectCode(null);

                    problemVO.setFunctionConfig(functionConfig);
                    String[] substrings = judgeCases.getInput().split("\n");

                    StringBuilder cutInputCases = new StringBuilder();
                    int count = problemVO.getJudgeConfig().getTestCaseProvided() * problemVO.getFunctionConfig().getVarCount();
                    for (int i = 0; i < count && i < substrings.length; i++) {
                        cutInputCases.append(substrings[i]);
                        if (i < count - 1) {
                            cutInputCases.append("\n");
                        }
                    }
                    judgeCases.setInput(cutInputCases.toString());
                }
            } else {
                QueryWrapper<ProblemDislike> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("problemId",problemVO.getId());
                queryWrapper3.eq("userId",finalLoginUser.getId());
                if(problemDislikeMapper.selectCount(queryWrapper3) >0) {
                    problemVO.setIsDisliked(true);
                } else {
                    problemVO.setIsDisliked(false);
                }
                QueryWrapper<ProblemThumb> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("problemId",problemVO.getId());
                queryWrapper2.eq("userId",finalLoginUser.getId());
                if(problemThumbMapper.selectCount(queryWrapper2) >0) {
                    problemVO.setIsLiked(true);
                } else {
                    problemVO.setIsLiked(false);
                }
                QueryWrapper<Submission> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("userId", finalLoginUser.getId());
                queryWrapper1.eq("problemId", problemVO.getId());
                long count1 = submissionMapper.selectCount(queryWrapper1);
                queryWrapper1.eq("judgeStatus", SubmissionResultEnum.ACCEPT.getValue());
                long count2 = submissionMapper.selectCount(queryWrapper1);
                if (count2 > 0) {
                    problemVO.setIsSolved(1);
                } else if (count1 > 0) {
                    problemVO.setIsSolved(-1);
                } else {
                    problemVO.setIsSolved(0);
                }
            }

            Long problemId = problem.getId();
            Long submitData = problemMapper.getSubmitTimes(problemId);
            Long submitAc = problemMapper.getSubmitAccepted(problemId);
            problemVO.setAccpetedCount(submitAc);
            problemVO.setSubmittedCount(submitData);
            if (Objects.equals(.0, Double.parseDouble(submitData + ""))) {
                problemVO.setAcceptance(.0);
            } else {
                problemVO.setAcceptance(Double.parseDouble(submitAc + "") / Double.parseDouble(submitData + ""));
            }
            return problemVO;
        }).collect(Collectors.toList());
        problemVOPage.setRecords(problemVOList);
        return problemVOPage;
    }

    public List<Problem> getProblemQueryRes(long current, long size, String title, Integer difficulty, Integer status, HttpServletRequest request) {
        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
        }
        long offset = Math.max(0, (current - 1)) * size;
        return problemMapper.getProblemQueryRes(size, offset, title, difficulty, status, loginUser == null ? -1 : loginUser.getId());
    }

    public Integer getProblemQueryCount(long current, long size, String title, Integer difficulty, Integer status, HttpServletRequest request) {
        User loginUser = null;
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj != null) {
            loginUser = userService.getLoginUser(request);
        }
        long offset = Math.max(0, (current - 1)) * size;
        return problemMapper.getProblemQueryCount(size, offset, title, difficulty, status, loginUser == null ? -1 : loginUser.getId());
    }
    public boolean doLikeProblem(Long problemId, Long userId, HttpServletRequest request) {
        QueryWrapper<ProblemThumb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problemId", problemId);
        queryWrapper.eq("userId", userId);
        Long count = problemThumbMapper.selectCount(queryWrapper);
        Problem problem = this.getById(problemId);
        if(Objects.equals(0L, count)) {
            problem.setThumbNum(problem.getThumbNum()+1);
            this.updateById(problem);
            ProblemThumb problemThumb = new ProblemThumb();
            problemThumb.setProblemId(problemId);
            problemThumb.setUserId(userId);
            return problemThumbService.save(problemThumb);
        } else {
            problem.setThumbNum(problem.getThumbNum()-1);
            this.updateById(problem);
            return problemThumbService.remove(queryWrapper);
        }
    }
    public boolean doDislikeProblem(Long problemId, Long userId, HttpServletRequest request) {
        QueryWrapper<ProblemDislike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("problemId", problemId);
        queryWrapper.eq("userId", userId);
        Long count = problemDislikeMapper.selectCount(queryWrapper);
        Problem problem = this.getById(problemId);
        if(Objects.equals(0L, count)) {
            problem.setDisLikeNum(problem.getDisLikeNum()+1);
            this.updateById(problem);
            ProblemDislike problemDislike = new ProblemDislike();
            problemDislike.setProblemId(problemId);
            problemDislike.setUserId(userId);
            return problemDislikeService.save(problemDislike);
        } else {
            problem.setDisLikeNum(problem.getDisLikeNum()-1);
            this.updateById(problem);
            return problemDislikeService.remove(queryWrapper);
        }
    }
}





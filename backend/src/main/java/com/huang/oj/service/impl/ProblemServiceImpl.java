package com.huang.oj.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.constant.CommonConstant;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.exception.ThrowUtils;
import com.huang.oj.mapper.ProblemMapper;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.problem.ProblemQueryRequest;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.User;
import com.huang.oj.model.vo.ProblemVO;
import com.huang.oj.model.vo.UserVO;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.UserService;
import com.huang.oj.utils.SqlUtils;
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


    @Override
    public void validProblem(Problem problem, boolean add) {
        if (problem == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = problem.getTitle();
        String content = problem.getContent();
        String solution = problem.getSolution();
        String tags = problem.getTags();
        JudgeConfig judgeConfig = JSON.parseObject(problem.getJudgeConfig(), JudgeConfig.class);
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags) && SqlUtils.isAnyNull(judgeConfig.getMemoryLimit(), judgeConfig.getStackLimit(), judgeConfig.getTimeLimit()), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
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
        String content = problemQueryRequest.getContent();

        QueryWrapper<Problem> queryWrapper1 = new QueryWrapper<>();
        // 尝试找id
        if (StringUtils.isNotBlank(title)) {
            queryWrapper1.eq("id", title);
            if (problemMapper.selectCount(queryWrapper1) != 0) {
                return queryWrapper1;
            }
        }

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.eq("isDelete", false);
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

        Long problemId = problem.getId();
        Long submitData = problemMapper.getSubmitTimes(problemId);
        Long submitAc = problemMapper.getSubmitAccepted(problemId);
        problemVO.setAccpetedCount(submitAc);
        problemVO.setSubmittedCount(submitData);
        if(Objects.equals(.0, Double.parseDouble(submitData + ""))) {
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
        // 填充信息
        List<ProblemVO> problemVOList = problemList.stream().map(problem -> {
            ProblemVO problemVO = ProblemVO.objToVo(problem);

            Long userId = problem.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                user = userService.getById(userId);
            }
            UserVO userVO = userService.getUserVO(user);
            problemVO.setUserVO(userVO);

            Long problemId = problem.getId();
            Long submitData = problemMapper.getSubmitTimes(problemId);
            Long submitAc = problemMapper.getSubmitAccepted(problemId);
            problemVO.setAccpetedCount(submitAc);
            problemVO.setSubmittedCount(submitData);
            if(Objects.equals(.0, Double.parseDouble(submitData + ""))) {
                problemVO.setAcceptance(.0);
            } else {
                problemVO.setAcceptance(Double.parseDouble(submitAc + "") / Double.parseDouble(submitData + ""));
            }
            return problemVO;
        }).collect(Collectors.toList());
        problemVOPage.setRecords(problemVOList);
        return problemVOPage;
    }

}





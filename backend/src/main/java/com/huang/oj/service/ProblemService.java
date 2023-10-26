package com.huang.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huang.oj.model.dto.problem.ProblemQueryRequest;
import com.huang.oj.model.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.oj.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Service
* @createDate 2023-09-27 16:06:56
*/
public interface ProblemService extends IService<Problem> {

    /**
     * 校验
     *
     * @param problem
     * @param add
     */
    void validProblem(Problem problem, boolean add);

    /**
     * 获取查询条件
     *
     * @param problemQueryRequest
     * @return
     */
    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param problem
     * @param request
     * @return
     */
    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param problemPage
     * @param request
     * @return
     */
    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);


    List<Problem> getProblemQueryRes(long current, long size, String title, Integer difficulty, Integer status,HttpServletRequest request);

    Integer getProblemQueryCount(long current, long size, String title, Integer difficulty, Integer status,HttpServletRequest request);

    boolean doLikeProblem(Long problemId, Long userId, HttpServletRequest request);

    boolean doDislikeProblem(Long problemId, Long userId, HttpServletRequest request);
}

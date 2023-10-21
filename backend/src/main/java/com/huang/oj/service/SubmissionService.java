package com.huang.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huang.oj.model.dto.problem.ProblemQueryRequest;
import com.huang.oj.model.dto.submission.ProblemSubmitQuest;
import com.huang.oj.model.dto.submission.SubmissionQueryQuest;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.Submission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.oj.model.entity.User;
import com.huang.oj.model.vo.ProblemVO;
import com.huang.oj.model.vo.SimpleSubmissionVO;
import com.huang.oj.model.vo.SubmissionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【submission(提交)】的数据库操作Service
* @createDate 2023-09-26 21:03:14
*/
public interface SubmissionService extends IService<Submission> {

    /**
     * 点赞
     *
     * @param problemSubmitQuest
     * @param loginUser
     * @return
     */
    void ValidSubmission(ProblemSubmitQuest problemSubmitQuest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param submissionQueryQuest
     * @return
     */
    QueryWrapper<Submission> getQueryWrapper(SubmissionQueryQuest submissionQueryQuest);

    /**
     * 获取帖子封装
     *
     * @param submission
     * @param request
     * @return
     */
    SubmissionVO getSubmissionVO(Submission submission, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param submissionPage
     * @param request
     * @return
     */
    Page<SubmissionVO> getSubmissionVOPage(Page<Submission> submissionPage, HttpServletRequest request);

    List<SimpleSubmissionVO> getSimpleSubmissionPage(long current, long size, String title, Integer judgeStatus, HttpServletRequest request);

    Integer getSimpleSubmissionCount(long current, long size, String title, Integer judgeStatus, HttpServletRequest request);
}

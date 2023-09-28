package com.huang.oj.model.vo;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.*;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.Submission;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交
 *
 * @TableName submission
 */
@Data
public class SubmissionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 语言
     */
    private String language;

    /**
     * 提交的代码
     */
    private String code;

    /**
     * 结果
     */
    private JudgeInfo judgeInfo;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 用户
     */
    private UserVO userVO;
    /**
     * 题目
     */
    private ProblemVO problemVO;

    public static Submission voToObj(SubmissionVO submissionVO) {
        if (submissionVO == null) {
            return null;
        }
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionVO, submission);
        JudgeInfo judgeInfo1 = submissionVO.getJudgeInfo();
        if (judgeInfo1 != null) {
            submission.setJudgeInfo(judgeInfo1.toString());
        }
        return submission;
    }

    /**
     * 对象转包装类
     *
     * @param submission
     * @return
     */
    public static SubmissionVO objToVo(Submission submission) {
        if (submission == null) {
            return null;
        }
        SubmissionVO submissionVO = new SubmissionVO();
        BeanUtils.copyProperties(submission, submissionVO);
        String judgeInfo1 = submission.getJudgeInfo();
        if (StringUtils.isNotBlank(judgeInfo1)) {
            submissionVO.setJudgeInfo(JSON.parseObject(judgeInfo1, JudgeInfo.class));
        }
        return submissionVO;
    }
}
package com.yi.oj.model.vo;

import com.alibaba.fastjson.JSON;
import com.yi.oj.judge.sandbox.model.JudgeResult;
import com.yi.oj.model.entity.Submission;
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
    private JudgeResult judgeResult;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 用户
     */
    private UserVO userVO;

    public static Submission voToObj(SubmissionVO submissionVO) {
        if (submissionVO == null) {
            return null;
        }
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionVO, submission);
        JudgeResult judgeResult1 = submissionVO.getJudgeResult();
        if (judgeResult1 != null) {
            submission.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult1));
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
        String judgeResult1 = submission.getJudgeResult();
        if (StringUtils.isNotBlank(judgeResult1)) {
            submissionVO.setJudgeResult(JSON.parseObject(judgeResult1, JudgeResult.class));
        }
        return submissionVO;
    }
}
package com.huang.oj.model.vo;

import com.alibaba.fastjson.JSON;
import com.huang.oj.judge.sandbox.model.JudgeResult;
import com.huang.oj.model.entity.Submission;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交简易版
 */
@Data
public class SimpleSubmissionVO implements Serializable {
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
     * 题目
     */
    private String title;

    /**
     * 结果
     */
    private JudgeResult judgeResult;

    /**
     * 提交时间
     */
    private Date submitTime;

}
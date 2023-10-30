package com.yi.oj.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交简易版
 */
@Data
public class SimpleSubmission implements Serializable {
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
    private String judgeResult;

    /**
     * 提交时间
     */
    private Date submitTime;

}
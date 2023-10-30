package com.yioj.model.model.dto.submission;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 
 
 */
@Data
public class ProblemSubmitQuest implements Serializable {

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

}
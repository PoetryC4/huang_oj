package com.yi.oj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 
 
 */
@Data
public class ProblemThumbUpdateRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long problemId;

    private static final long serialVersionUID = 1L;
}
package com.yioj.model.model.dto.problem;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点踩请求
 *
 
 
 */
@Data
public class ProblemDislikeUpdateRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long problemId;

    private static final long serialVersionUID = 1L;
}
package com.huang.oj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ProblemThumbUpdateRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long problemId;

    private static final long serialVersionUID = 1L;
}
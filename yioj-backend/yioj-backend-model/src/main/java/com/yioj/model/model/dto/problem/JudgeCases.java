package com.yioj.model.model.dto.problem;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 
 */
@Data
public class JudgeCases implements Serializable {
    /**
     * 输入
     */
    private String input;
    /**
     * 预期
     */
    private String expected;
}
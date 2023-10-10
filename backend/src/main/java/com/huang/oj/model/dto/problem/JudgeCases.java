package com.huang.oj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
package com.huang.oj.model.dto.problem;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FunctionConfig {
    /**
     * 默认填充函数
     */
    private Map<String, String> defaultCode;
    /**
     * 用于初步处理的函数
     */

    private Map<String, String> initCode;
    /**
     * 正确的函数
     */

    private Map<String, String> correctCode;
    /**
     * 输入参数个数
     */

    private Integer varCount;
}

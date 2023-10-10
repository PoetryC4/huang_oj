package com.huang.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum SubmissionResultEnum {

    ACCEPT("Accepted", "Accepted"),
    COMPILE_ERROR("Compile Error", "Compile Error"),
    WRONG_ANSWER("Wrong Answer", "Wrong Answer"),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "Memory Limit Exceeded"),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "Time Limit Exceeded"),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "Output Limit Exceeded"),
    WAITING("Waiting", "Waiting"),
    DANGEROUS_OPERATION("Dangerous Operation", "Dangerous Operation"),
    RUNTIME_ERROR("Runtime Error", "Runtime Error"),
    SYSTEM_ERROR("System Error", "System Error");

    private final String text;

    private final String value;

    SubmissionResultEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SubmissionResultEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SubmissionResultEnum anEnum : SubmissionResultEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

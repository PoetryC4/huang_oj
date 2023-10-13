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

    ACCEPT("Accepted", 0),
    COMPILE_ERROR("Compile Error", 1),
    WRONG_ANSWER("Wrong Answer", 2),
    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", 3),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", 4),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", 5),
    WAITING("Waiting", 6),
    DANGEROUS_OPERATION("Dangerous Operation", 7),
    RUNTIME_ERROR("Runtime Error", 8),
    SYSTEM_ERROR("System Error", 9),
    SUBMITTING("提交中", 10),
    COMPILING("编译中", 11),
    RUNNING("运行中", 12),
    JUDGING("判断中", 13);

    private final String text;

    private final Integer value;

    SubmissionResultEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SubmissionResultEnum getEnumByValue(Integer value) {
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

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}

package com.yi.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目时间枚举
 *
 */
public enum ProblemLanguageTimeEnum {

    C("C", 0),
    CPLUSPLUS("C++", 0),
    JAVA("Java", 500 * 1024),
    PYTHON("Python", 0),
    PYTHON3("Python3", 0),
    CSHARP("C#", 0),
    JAVASCRIPT("JavaScript", 200 * 1024),
    TYPESCRIPT("TypeScript", 200 * 1024),
    PHP("PHP", 0);

    private final String text;

    private final Integer value;

    ProblemLanguageTimeEnum(String text, Integer value) {
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
    public static ProblemLanguageTimeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ProblemLanguageTimeEnum anEnum : ProblemLanguageTimeEnum.values()) {
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

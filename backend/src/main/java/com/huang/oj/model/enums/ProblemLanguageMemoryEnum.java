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
public enum ProblemLanguageMemoryEnum {

    C("C", 0),
    CPLUSPLUS("C++", 0),
    JAVA("Java", 50),
    PYTHON("Python", 0),
    PYTHON3("Python3", 0),
    CSHARP("C#", 0),
    JAVASCRIPT("JavaScript", 0),
    TYPESCRIPT("TypeScript", 0),
    PHP("PHP", 0);

    private final String text;

    private final Integer value;

    ProblemLanguageMemoryEnum(String text, Integer value) {
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
    public static ProblemLanguageMemoryEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ProblemLanguageMemoryEnum anEnum : ProblemLanguageMemoryEnum.values()) {
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

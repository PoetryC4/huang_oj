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
public enum ProblemLanguageEnum {

    C("C", "C"),
    CPLUSPLUS("C++", "C++"),
    JAVA("Java", "Java"),
    PYTHON("Python", "Python"),
    PYTHON3("Python3", "Python3"),
    CSHARP("C#", "C#"),
    JAVASCRIPT("JavaScript", "JavaScript"),
    TYPESCRIPT("TypeScript", "TypeScript"),
    PHP("PHP", "PHP"),
    BAT("bat", "bat"),
    SQL("SQL", "SQL"),
    MYSQL("MySQL", "MySQL"),
    SHELL("shell", "shell"),
    KOTLIN("Kotlin", "Kotlin"),
    RUST("rust", "rust"),
    GOLANG("GoLang","GoLang");

    private final String text;

    private final String value;

    ProblemLanguageEnum(String text, String value) {
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
    public static ProblemLanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ProblemLanguageEnum anEnum : ProblemLanguageEnum.values()) {
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

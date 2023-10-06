package com.huang.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 */
public enum SubmissionStatusEnum {

    SUBMITTING("提交中", 0),
    COMPILING("编译中", 1),
    RUNNING("运行中", 2),
    JUDGING("判断中", 3),
    FAILED("失败", 4),
    SUCCESS("成功", 5);

    private final String text;

    private final Integer value;

    SubmissionStatusEnum(String text, Integer value) {
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
    public static SubmissionStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SubmissionStatusEnum anEnum : SubmissionStatusEnum.values()) {
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

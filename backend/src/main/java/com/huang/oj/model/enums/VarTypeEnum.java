package com.huang.oj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
public enum VarTypeEnum {

    VOID("void", 0),
    INT("int", 1),
    CHAR("char", 2),
    INT_ARRAY("int[]", 3),
    INTEGER_LIST("List<Integer>", 4),
    DOUBLE("double", 5),
    DOUBLE_ARRAY("double[]", 6),
    DOUBLE_LIST("List<Double>", 7),
    STRING_ARRAY("String[]", 8),
    STRING_LIST("List<String>", 9),
    STRING("String", 10),
    LIST_NODE("ListNode", 11), // 链表节点
    TREE_NODE("TreeNode", 12), // 树结点
    BOOLEAN("boolean", 13),
    BOOLEAN_Array("boolean[]", 14),
    BOOLEAN_LIST("List<Boolean>", 15),
    INT_ARRAY_ARRAY("int[][]", 16),
    INTEGER_LIST_LIST("List<List<Integer>>", 17),
    STRING_ARRAY_ARRAY("STRING[][]", 18),
    STRING_LIST_LIST("List<List<STRING>>", 19);

    private final String text;

    private final Integer value;

    VarTypeEnum(String text, Integer value) {
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
    public static VarTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (VarTypeEnum anEnum : VarTypeEnum.values()) {
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

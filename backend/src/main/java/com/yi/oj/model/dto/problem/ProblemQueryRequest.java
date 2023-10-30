package com.yi.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yi.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 
 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目标题
     */
    private String title;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * vip尊享?
     */
    private Integer isVip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
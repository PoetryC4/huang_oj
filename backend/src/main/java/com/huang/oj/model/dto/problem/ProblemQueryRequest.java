package com.huang.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huang.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
package com.huang.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ProblemTag implements Serializable {
    /**
     * 难度 0-easy
     */
    private Integer difficulty;
    /**
     * 类型
     */
    private List<String> types;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
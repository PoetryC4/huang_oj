package com.huang.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class JudgeConfig implements Serializable {
    /**
     * 提示
     */
    private String hint;
    /**
     * 限制
     */
    private String[] constraints;//JSON
    /**
     * 时间限制
     */
    private Long timeLimit;
    /**
     * 内存限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
    /**
     * 提供样例数
     */
    private Integer testCaseProvided;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
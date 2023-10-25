package com.huang.oj.model.dto.problem;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 创建请求
 *
 
 
 */
@Data
public class JudgeConfig implements Serializable {
    /**
     * 提示
     */
    private String hint;
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
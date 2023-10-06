package com.huang.oj.model.vo;

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
public class JudgeCaseVO implements Serializable {
    /**
     * 输入
     */
    private String input;
    /**
     * print输出
     */
    private String stdout;
    /**
     * 输出
     */
    private String output;
    /**
     * 预期
     */
    private String expected;
    /**
     * 运行时
     */
    private Long runtime;
    /**
     * 运行内存
     */
    private Long memory;
    /**
     * 结果
     */
    private String result;
    /**
     * 错误代码
     */
    private String detailCode;

}
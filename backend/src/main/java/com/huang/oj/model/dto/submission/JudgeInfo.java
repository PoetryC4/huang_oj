package com.huang.oj.model.dto.submission;

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
public class JudgeInfo implements Serializable {
    /**
     * 时间
     */
    private Long timeUsed;
    /**
     * 内存
     */
    private Long memoryUsed;
    /**
     * 结果
     */
    private String resultStr;
    /**
     * 出错代码
     */
    private String detailCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
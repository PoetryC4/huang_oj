package com.huang.oj.model.vo;

import com.huang.oj.model.dto.submission.JudgeInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ProblemQueryVO implements Serializable {

    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 语言
     */
    private String language;

    /**
     * 提交的代码
     */
    private String code;

    /**
     * 信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 当前状态 0-编译 1-运行 2-失败 3-成功
     */
    private Integer judgeStatus;

}
package com.yi.oj.model.vo;

import com.yi.oj.model.dto.submission.JudgeInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目请求VO
 *
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
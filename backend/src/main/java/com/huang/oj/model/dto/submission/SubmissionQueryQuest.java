package com.huang.oj.model.dto.submission;

import com.baomidou.mybatisplus.annotation.*;
import com.huang.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交
 * @TableName submission
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmissionQueryQuest extends PageRequest implements Serializable {

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
     * 当前状态
     */
    private Integer judgeStatus;
}
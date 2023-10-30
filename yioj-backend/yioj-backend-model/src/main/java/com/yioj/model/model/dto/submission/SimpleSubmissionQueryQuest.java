package com.yioj.model.model.dto.submission;

import com.yioj.common.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 提交
 * @TableName submission
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleSubmissionQueryQuest extends PageRequest implements Serializable {

    /**
     * 提交用户 id
     */
    private Long userId;
    /**
     * 题目
     */
    private String title;

    /**
     * 当前状态
     */
    private Integer judgeStatus;

}
package com.huang.oj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 提交
 * @TableName submission
 */
@TableName(value ="submission")
@Data
public class Submission implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 当前状态 0-编译 1-运行 2-失败 3-成功
     */
    private Integer judgeStatus;

    /**
     * 结果 
     */
    private String judgeInfo;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 是否删除
     */
    @TableLogic(value="0",delval="1")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.huang.oj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 题
 *
 * @TableName problem
 */
@TableName(value = "problem")
@Data
public class Problem implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private String judgeConfig;

    /**
     * 检测样例（json 数组）
     */
    private String judgeCase;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 点踩数
     */
    private Integer disLikeNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * vip尊享?
     */
    private Integer isVip;

    /**
     * 是否删除
     */
    @TableLogic(value="0",delval="1")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
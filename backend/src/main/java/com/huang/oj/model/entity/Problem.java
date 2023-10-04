package com.huang.oj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.huang.oj.model.dto.problem.JudgeCase;
import com.huang.oj.model.dto.problem.JudgeConfig;
import lombok.Data;

/**
 * 题
 * @TableName problem
 */
@TableName(value ="problem")
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
     * 官解
     */
    private String solution;
    /**
     * 内容
     */
    private String content;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 题目标签列表
     */
    private String tags;

    /**
     * 判题标签列表
     */
    private String judgeConfig;

    /**
     * 检测样例
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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
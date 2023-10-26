package com.huang.oj.model.vo;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.gson.Gson;
import com.huang.oj.model.dto.problem.FunctionConfig;
import com.huang.oj.model.dto.problem.JudgeCases;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.entity.Comment;
import com.huang.oj.model.entity.Problem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论VO
 *
 *
 */
@Data
public class CommentVO implements Serializable {
    private final static Gson GSON = new Gson();

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 用户
     */
    private UserVO userVO;
    /**
     * 题目
     */
    private ProblemVO problemVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public static Comment voToObj(CommentVO commentVO) {
        if (commentVO == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);
        return comment;
    }

    public static CommentVO objToVo(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
    }
}
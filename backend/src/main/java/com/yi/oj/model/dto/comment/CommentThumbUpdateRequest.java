package com.yi.oj.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 

 */
@Data
public class CommentThumbUpdateRequest implements Serializable {

    /**
     * 评论 id
     */
    private Long commentId;

    private static final long serialVersionUID = 1L;
}
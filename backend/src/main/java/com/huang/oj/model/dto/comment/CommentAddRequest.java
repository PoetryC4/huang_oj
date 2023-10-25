package com.huang.oj.model.dto.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 
 
 */
@Data
public class CommentAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long problemId;

    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}
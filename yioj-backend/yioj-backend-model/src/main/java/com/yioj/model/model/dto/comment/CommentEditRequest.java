package com.yioj.model.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 */
@Data
public class CommentEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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
package com.yi.oj.model.dto.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 

 */
@Data
public class CommentUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}
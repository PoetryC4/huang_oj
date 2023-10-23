package com.huang.oj.model.dto.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
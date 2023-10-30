package com.yi.oj.model.dto.comment;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

import com.yi.oj.model.entity.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 帖子 ES 包装类
 *
 **/
// todo 取消注释开启 ES（须先配置 ES）
@Document(indexName = "comment")
@Data
public class CommentEsDTO implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * id
     */
    @Field(type = FieldType.Long)
    private Long problemId;

    /**
     * 内容
     */
    @Field(type = FieldType.Text)
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 创建用户 id
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;

    private static final Gson GSON = new Gson();

    /**
     * 对象转包装类
     *
     * @param comment
     * @return
     */
    public static CommentEsDTO objToDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentEsDTO commentEsDTO = new CommentEsDTO();
        BeanUtils.copyProperties(comment, commentEsDTO);
        return commentEsDTO;
    }

    /**
     * 包装类转对象
     *
     * @param commentEsDTO
     * @return
     */
    public static Comment dtoToObj(CommentEsDTO commentEsDTO) {
        if (commentEsDTO == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentEsDTO, comment);
        return comment;
    }
}

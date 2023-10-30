package com.yioj.commentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yioj.model.model.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Mapper
* @createDate 2023-09-27 16:06:56
* @Entity com.yi.oj.model.entity.Problem
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询评论列表（包括已被删除的数据）
     */
    List<Comment> listPostWithDelete(Date minUpdateTime);
}





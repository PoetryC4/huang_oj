package com.huang.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huang.oj.model.entity.Comment;
import com.huang.oj.model.entity.ProblemThumb;

import java.util.Date;
import java.util.List;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Mapper
* @createDate 2023-09-27 16:06:56
* @Entity com.huang.oj.model.entity.Problem
*/
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 查询评论列表（包括已被删除的数据）
     */
    List<Comment> listPostWithDelete(Date minUpdateTime);
}





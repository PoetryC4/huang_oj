package com.yi.oj.mapper;

import com.yi.oj.model.entity.Problem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Mapper
* @createDate 2023-09-27 16:06:56
* @Entity com.yi.oj.model.entity.Problem
*/
public interface ProblemMapper extends BaseMapper<Problem> {

    Long getSubmitTimes(Long problemId);

    Long getSubmitAccepted(Long problemId);

    List<Problem> getProblemQueryRes(long size, long offset, String title, Integer difficulty, Integer status, Long userId);
    Integer getProblemQueryCount(long size, long offset, String title, Integer difficulty, Integer status, Long userId);
}





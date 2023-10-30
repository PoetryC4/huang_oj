package com.yioj.problemservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yioj.model.model.entity.ProblemDislike;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Mapper
* @createDate 2023-09-27 16:06:56
* @Entity com.yi.oj.model.entity.Problem
*/
@Mapper
public interface ProblemDislikeMapper extends BaseMapper<ProblemDislike> {
}





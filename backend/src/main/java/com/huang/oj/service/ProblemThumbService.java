package com.huang.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.oj.model.dto.problem.ProblemQueryRequest;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.ProblemThumb;
import com.huang.oj.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Service
* @createDate 2023-09-27 16:06:56
*/
public interface ProblemThumbService extends IService<ProblemThumb> {
}

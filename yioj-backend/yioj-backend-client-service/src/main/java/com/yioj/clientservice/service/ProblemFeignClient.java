package com.yioj.clientservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yioj.model.model.dto.problem.ProblemQueryRequest;
import com.yioj.model.model.entity.Problem;
import com.yioj.model.model.vo.ProblemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【problem(题)】的数据库操作Service
 * @createDate 2023-09-27 16:06:56
 */
@FeignClient(name = "yioj-backend-problem-service", path = "/api/problem/inner")
public interface ProblemFeignClient {

    @GetMapping("/get/id")
    Problem getById(@RequestParam("problemId") long problemId);
}

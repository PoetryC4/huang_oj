package com.yioj.clientservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yioj.model.model.dto.submission.ProblemSubmitQuest;
import com.yioj.model.model.dto.submission.SubmissionQueryQuest;
import com.yioj.model.model.entity.Submission;
import com.yioj.model.model.entity.User;
import com.yioj.model.model.vo.SimpleSubmissionVO;
import com.yioj.model.model.vo.SubmissionVO;
import com.yioj.model.model.vo.UserRecordVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【submission(提交)】的数据库操作Service
 * @createDate 2023-09-26 21:03:14
 */
@FeignClient(name = "yioj-backend-submission-service", path = "/api/submission/inner")
public interface SubmissionFeignClient {

    @GetMapping("/get/record")
    UserRecordVO getUserRecordTried(@RequestParam("userId") long userId);

    @PostMapping("/post/count")
    Long count(QueryWrapper<Submission> queryWrapper);
}

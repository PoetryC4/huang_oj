package com.yioj.submissionservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yioj.clientservice.service.SubmissionFeignClient;
import com.yioj.model.model.entity.Submission;
import com.yioj.model.model.vo.SubmissionVO;
import com.yioj.model.model.vo.UserRecordVO;
import com.yioj.submissionservice.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class SubmissionInnerController implements SubmissionFeignClient {
    @Resource
    private SubmissionService submissionService;

    @GetMapping("/get/record")
    public UserRecordVO getUserRecordTried(@RequestParam("userId") long userId) {
        return submissionService.getUserRecordTried(userId);
    }

    @PostMapping("/post/count")
    public Long count(QueryWrapper<Submission> queryWrapper) {
        return submissionService.count(queryWrapper);
    }
}

package com.yioj.problemservice.controller.inner;

import com.yioj.clientservice.service.ProblemFeignClient;
import com.yioj.model.model.entity.Problem;
import com.yioj.problemservice.service.ProblemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class ProblemInnerController implements ProblemFeignClient {

    @Resource
    private ProblemService problemService;

    @GetMapping("/get/id")
    public Problem getById(@RequestParam("problemId") long problemId) {
        return problemService.getById(problemId);
    }
}

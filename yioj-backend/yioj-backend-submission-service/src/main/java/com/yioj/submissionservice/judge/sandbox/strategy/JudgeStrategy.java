package com.yioj.submissionservice.judge.sandbox.strategy;

import com.yioj.model.model.dto.problem.JudgeConfig;

public interface JudgeStrategy {

    public Integer judgeLimit(String language, Long timeUsed, Long memoryUsed, JudgeConfig judgeConfig);
}

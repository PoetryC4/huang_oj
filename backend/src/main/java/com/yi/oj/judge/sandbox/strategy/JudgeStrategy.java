package com.yi.oj.judge.sandbox.strategy;

import com.yi.oj.model.dto.problem.JudgeConfig;

public interface JudgeStrategy {

    public Integer judgeLimit(String language, Long timeUsed, Long memoryUsed, JudgeConfig judgeConfig);
}

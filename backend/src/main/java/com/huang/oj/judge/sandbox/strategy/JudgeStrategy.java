package com.huang.oj.judge.sandbox.strategy;

import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import org.springframework.stereotype.Service;

public interface JudgeStrategy {

    public Integer judgeLimit(String language, Long timeUsed, Long memoryUsed, JudgeConfig judgeConfig);
}

package com.huang.oj.judge.sandbox.strategy;

import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;

public interface JudgeStrategy {

    boolean judgeLimit(String language, JudgeInfo judgeInfo, JudgeConfig judgeConfig);
}

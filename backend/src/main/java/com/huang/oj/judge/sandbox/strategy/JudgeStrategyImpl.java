package com.huang.oj.judge.sandbox.strategy;

import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.enums.ProblemLanguageMemoryEnum;
import com.huang.oj.model.enums.ProblemLanguageTimeEnum;
import com.huang.oj.model.enums.SubmissionResultEnum;

public class JudgeStrategyImpl implements JudgeStrategy {
    @Override
    public boolean judgeLimit(String language, JudgeInfo judgeInfo, JudgeConfig judgeConfig) {
        if (judgeInfo.getMemoryUsed() > (ProblemLanguageMemoryEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageMemoryEnum.getEnumByValue(language).getValue()) + judgeConfig.getMemoryLimit()) {
            judgeInfo.setResultStr(SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getValue());
            return false;
        } else if (judgeInfo.getTimeUsed() > (ProblemLanguageTimeEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageTimeEnum.getEnumByValue(language).getValue()) + judgeConfig.getTimeLimit()) {
            judgeInfo.setResultStr(SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return false;
        }
        return true;
    }
}

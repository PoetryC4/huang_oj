package com.huang.oj.judge.sandbox.strategy;

import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.enums.ProblemLanguageMemoryEnum;
import com.huang.oj.model.enums.ProblemLanguageTimeEnum;
import com.huang.oj.model.enums.SubmissionResultEnum;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

@Service
public class JudgeStrategyImpl implements JudgeStrategy {
    @Override
    public String judgeLimit(String language, Long timeUsed, Long memoryUsed, JudgeConfig judgeConfig) {
        if (memoryUsed / Math.pow(2, 20) > (ProblemLanguageMemoryEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageMemoryEnum.getEnumByValue(language).getValue()) + judgeConfig.getMemoryLimit()) {
            return SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getValue();
        } else if (timeUsed > (ProblemLanguageTimeEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageTimeEnum.getEnumByValue(language).getValue()) + judgeConfig.getTimeLimit()) {
            return SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getValue();
        }
        return SubmissionResultEnum.ACCEPT.getValue();
    }
}

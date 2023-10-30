package com.yioj.submissionservice.judge.sandbox.strategy;

import com.yioj.model.model.dto.problem.JudgeConfig;
import com.yioj.model.model.enums.ProblemLanguageMemoryEnum;
import com.yioj.model.model.enums.ProblemLanguageTimeEnum;
import com.yioj.model.model.enums.SubmissionResultEnum;
import org.springframework.stereotype.Service;

@Service
public class JudgeStrategyImpl implements JudgeStrategy {
    @Override
    public Integer judgeLimit(String language, Long timeUsed, Long memoryUsed, JudgeConfig judgeConfig) {
        if (memoryUsed / Math.pow(2, 20) > (ProblemLanguageMemoryEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageMemoryEnum.getEnumByValue(language).getValue()) + judgeConfig.getMemoryLimit()) {
            return SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getValue();
        } else if (timeUsed > (ProblemLanguageTimeEnum.getEnumByValue(language) == null ? 0 : ProblemLanguageTimeEnum.getEnumByValue(language).getValue()) + judgeConfig.getTimeLimit()) {
            return SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getValue();
        }
        return SubmissionResultEnum.ACCEPT.getValue();
    }
}

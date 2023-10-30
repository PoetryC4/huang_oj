package com.yi.oj.judge;

import com.yi.oj.judge.sandbox.model.JudgeResult;
import com.yi.oj.model.dto.submission.ProblemTestExampleRequest;

public interface JudgeService {

    JudgeResult doJudge(long submissionId);

    JudgeResult testJudge(ProblemTestExampleRequest problemTestExampleRequest);
}

package com.huang.oj.judge;

import com.huang.oj.judge.sandbox.model.JudgeResult;
import com.huang.oj.model.dto.submission.ProblemTestExampleRequest;

public interface JudgeService {

    JudgeResult doJudge(long submissionId);

    JudgeResult testJudge(ProblemTestExampleRequest problemTestExampleRequest);
}

package com.yioj.submissionservice.judge;

import com.yioj.model.model.dto.submission.ProblemTestExampleRequest;
import com.yioj.model.model.judge.JudgeResult;

public interface JudgeService {

    JudgeResult doJudge(long submissionId);

    JudgeResult testJudge(ProblemTestExampleRequest problemTestExampleRequest);
}

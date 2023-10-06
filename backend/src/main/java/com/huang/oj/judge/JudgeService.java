package com.huang.oj.judge;

import com.huang.oj.judge.sandbox.model.JudgeResult;

public interface JudgeService {

    JudgeResult doJudge(long submissionId);
}

package com.huang.oj.judge.sandbox.impl;

import com.huang.oj.judge.sandbox.CodeSandbox;
import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.CodeExecuteResponse;
import com.huang.oj.model.dto.submission.JudgeInfo;

public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public CodeExecuteResponse executeCode(CodeExecuteRequest codeExecuteRequest) {
        CodeExecuteResponse codeExecuteResponse = new CodeExecuteResponse();
        codeExecuteResponse.setCode(codeExecuteRequest.getCode());
        codeExecuteResponse.setLanguage(codeExecuteRequest.getLanguage());
        codeExecuteResponse.setInputCases(codeExecuteRequest.getInputCases());
        codeExecuteResponse.setOutputCases(codeExecuteRequest.getInputCases());// TODO
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTimeUsed(200L);
        judgeInfo.setMemoryUsed(200L);
        codeExecuteResponse.setJudgeInfo(judgeInfo);
        return codeExecuteResponse;
    }
}

package com.huang.oj.judge.sandbox.impl;

import com.huang.oj.judge.sandbox.CodeSandbox;
import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.CodeExecuteResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox _codeSandbox) {
        codeSandbox = _codeSandbox;
    }

    @Override
    public CodeExecuteResponse executeCode(CodeExecuteRequest codeExecuteRequest) {
        log.info(codeExecuteRequest.toString());
        CodeExecuteResponse codeExecuteResponse = codeSandbox.executeCode(codeExecuteRequest);
        log.info(codeExecuteResponse.toString());
        return codeExecuteResponse;
    }
}

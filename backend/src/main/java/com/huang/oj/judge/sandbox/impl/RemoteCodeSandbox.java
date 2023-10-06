package com.huang.oj.judge.sandbox.impl;

import com.huang.oj.judge.sandbox.CodeSandbox;
import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.CodeExecuteResponse;

public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public CodeExecuteResponse executeCode(CodeExecuteRequest codeExecuteRequest) {
        System.out.println("RemoteCodeSandbox");
        return null;
    }
}

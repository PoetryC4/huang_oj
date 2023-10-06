package com.huang.oj.judge.sandbox;

import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.CodeExecuteResponse;

public interface CodeSandbox {

    CodeExecuteResponse executeCode(CodeExecuteRequest codeExecuteRequest);
}

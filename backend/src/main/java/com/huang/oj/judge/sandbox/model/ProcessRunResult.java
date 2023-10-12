package com.huang.oj.judge.sandbox.model;

import lombok.Data;

@Data
public class ProcessRunResult {
    private String runtime;
    private String memoryUsed;
    private String stdOut;
    private String funcReturn;
    private String errMessage;
}

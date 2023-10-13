package com.huang.oj.judge.sandbox.model;

import lombok.Data;

import java.util.List;

@Data
public class ProcessRunResult {
    private List<Long> runtime;
    private List<Long> memoryUsed;
    private List<String> stdOut;
    private List<String> funcReturn;
    private String errMessage;
}

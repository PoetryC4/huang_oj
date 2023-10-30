package com.yioj.model.model.judge;

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

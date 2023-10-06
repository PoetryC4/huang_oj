package com.huang.oj.judge.sandbox.model;

import com.huang.oj.model.dto.submission.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeExecuteResponse {

    private String code;
    private String language;
    private Integer result;
    private String message;
    private List<String> inputCases;
    private List<String> outputCases;
    private JudgeInfo judgeInfo;
}

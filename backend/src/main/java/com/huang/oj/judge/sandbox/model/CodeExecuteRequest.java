package com.huang.oj.judge.sandbox.model;

import com.huang.oj.model.dto.problem.FunctionConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeExecuteRequest {

    private String code;
    private String language;
    private String inputCases;
    private FunctionConfig functionConfig;
}

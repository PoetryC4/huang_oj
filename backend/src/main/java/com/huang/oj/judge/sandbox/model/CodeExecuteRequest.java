package com.huang.oj.judge.sandbox.model;

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

    private String initCode;
    private String correctCode;
    private String code;
    private String language;
    private String inputCases;
}

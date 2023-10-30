package com.yioj.model.model.judge;

import com.yioj.model.model.dto.problem.FunctionConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

package com.yioj.model.model.dto.submission;

import com.yioj.model.model.dto.problem.JudgeCases;
import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 
 
 */
@Data
public class ProblemTestExampleRequest implements Serializable {
    /**
     * 样例（回车分割）
     */
    private JudgeCases judgeCases;

    /**
     * 基本判题信息
     */

    private ProblemSubmitQuest problemSubmitQuest;

}
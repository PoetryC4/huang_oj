package com.huang.oj.model.dto.submission;

import com.huang.oj.model.dto.problem.JudgeCases;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
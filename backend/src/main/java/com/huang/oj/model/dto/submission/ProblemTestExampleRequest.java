package com.huang.oj.model.dto.submission;

import com.huang.oj.model.dto.problem.JudgeCases;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
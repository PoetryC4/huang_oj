package com.huang.oj.model.dto.submission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huang.oj.model.dto.problem.JudgeCase;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
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
     * 样例（json 数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 基本判题信息
     */

    private ProblemSubmitQuest problemSubmitQuest;

}
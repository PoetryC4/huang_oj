package com.yi.oj.judge.sandbox.model;


import com.yi.oj.model.dto.submission.JudgeInfo;
import com.yi.oj.model.vo.JudgeCaseVO;
import lombok.Data;

import java.util.List;

@Data
public class JudgeResult {
    /**
     * 出错的那个case
     */
    private JudgeCaseVO judgeCaseVO;
    /**
     * 信息
     */

    private JudgeInfo judgeInfo;
    /**
     * 所有case的运行结果
     */

    private List<JudgeCaseVO> judgeCaseVOList;
    /**
     * 提交id
     */

    private Long submissionId;

}

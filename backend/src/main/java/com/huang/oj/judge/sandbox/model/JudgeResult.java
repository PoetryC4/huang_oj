package com.huang.oj.judge.sandbox.model;


import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.vo.JudgeCaseVO;
import lombok.Data;

import java.util.List;

@Data
public class JudgeResult {
    /**
     * 出错的那个case
     */
    private JudgeCaseVO judgeCaseVO;

    private JudgeInfo judgeInfo;

    private List<JudgeCaseVO> judgeCaseVOList;

    private Long submissionId;

}

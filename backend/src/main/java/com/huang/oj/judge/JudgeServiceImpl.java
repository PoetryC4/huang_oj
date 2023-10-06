package com.huang.oj.judge;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.judge.sandbox.CodeSandbox;
import com.huang.oj.judge.sandbox.CodeSandboxFactory;
import com.huang.oj.judge.sandbox.impl.CodeSandboxProxy;
import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.CodeExecuteResponse;
import com.huang.oj.judge.sandbox.model.JudgeResult;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategy;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategyImpl;
import com.huang.oj.mapper.SubmissionMapper;
import com.huang.oj.model.dto.problem.JudgeCase;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.model.enums.SubmissionStatusEnum;
import com.huang.oj.model.vo.JudgeCaseVO;
import com.huang.oj.model.vo.SubmissionVO;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.SubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private SubmissionService submissionService;

    @Resource
    private ProblemService problemService;

    @Resource
    private SubmissionMapper submissionMapper;

    @Value("${sandbox.type: example}")
    private String type;

    @Override
    public JudgeResult doJudge(long submissionId) {
        Submission submission = submissionService.getById(submissionId);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交不存在");
        }
        Problem problem = problemService.getById(submission.getProblemId());
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        QueryWrapper<Submission> queryWrapperSubmission = new QueryWrapper<>();
        queryWrapperSubmission.eq("isDelete", false);
        queryWrapperSubmission.eq("userId", submission.getUserId());
        queryWrapperSubmission.ne("judgeStatus", SubmissionStatusEnum.FAILED.getValue());
        queryWrapperSubmission.ne("judgeStatus", SubmissionStatusEnum.SUCCESS.getValue());
        if (submissionMapper.selectCount(queryWrapperSubmission) > 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "需等待当前判题结束");
        }
        Submission submission1 = new Submission();
        submission1.setId(submissionId);
        submission1.setJudgeStatus(SubmissionStatusEnum.COMPILING.getValue());
        submissionService.updateById(submission1);

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        CodeExecuteRequest codeExecuteRequest = new CodeExecuteRequest();
        codeExecuteRequest.setCode(submission.getCode());
        codeExecuteRequest.setLanguage(submission.getLanguage());
        String judgeCases1 = problem.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSON.parseArray(judgeCases1, JudgeCase.class);
        List<String> inputCases = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        codeExecuteRequest.setInputCases(inputCases);
        CodeExecuteResponse codeExecuteResponse = codeSandbox.executeCode(codeExecuteRequest);
        JudgeInfo judgeInfo = codeExecuteResponse.getJudgeInfo();

        List<String> outputCases = codeExecuteResponse.getOutputCases();
        List<String> expectedCases = judgeCaseList.stream().map(JudgeCase::getExpected).collect(Collectors.toList());
        boolean judge = false;
        if (outputCases.size() != expectedCases.size()) {
            submission1.setJudgeStatus(SubmissionStatusEnum.FAILED.getValue());
        } else {
            for (int i = 0; i < outputCases.size(); i++) {
                if (!Objects.equals(outputCases.get(i), expectedCases.get(i))) {
                    submission1.setJudgeStatus(SubmissionStatusEnum.FAILED.getValue());
                    // TODO 把错误定位在第i+1个样例并返回呈现错误细节
                    break;
                }
            }
            String judgeConfig1 = problem.getJudgeConfig();
            JudgeConfig judgeConfig = null;
            if (StringUtils.isNotBlank(judgeConfig1)) {
                judgeConfig = JSON.parseObject(judgeConfig1, JudgeConfig.class);
            }
            JudgeStrategy judgeStrategy = new JudgeStrategyImpl();
            judge = judgeStrategy.judgeLimit(submission.getLanguage(), judgeInfo, judgeConfig);
        }
        submission1.setJudgeStatus(judge ? SubmissionStatusEnum.SUCCESS.getValue() : SubmissionStatusEnum.FAILED.getValue());
        submissionService.updateById(submission1);
        JudgeResult judgeResult = new JudgeResult();
        if (judge) {
            judgeResult.setJudgeCaseVO(null);
        } else { // TODO 找到那个错误样例
            JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
            judgeCaseVO.setInput(inputCases.get(0));
            judgeCaseVO.setStdout(null);
            judgeCaseVO.setOutput(outputCases.get(0));
            judgeCaseVO.setExpected(expectedCases.get(0));
            judgeCaseVO.setRuntime(0L);
            judgeCaseVO.setMemory(0L);
            judgeCaseVO.setResult(judgeInfo.getResultStr());
            judgeCaseVO.setDetailCode(null);

            judgeResult.setJudgeCaseVO(judgeCaseVO);

        }
        judgeResult.setJudgeInfo(judgeInfo);
        List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
        for (int i = 0; i < inputCases.size(); i++) {
            JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
            judgeCaseVO.setInput(inputCases.get(i));
            judgeCaseVO.setOutput(outputCases.get(i));
            judgeCaseVO.setExpected(expectedCases.get(i));
            judgeCaseVOList.add(judgeCaseVO);
        }
        judgeResult.setJudgeCaseVOList(judgeCaseVOList);
        judgeResult.setSubmissionId(submissionId);
        return judgeResult;
    }
}

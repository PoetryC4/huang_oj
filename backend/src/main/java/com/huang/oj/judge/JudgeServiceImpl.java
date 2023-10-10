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
import com.huang.oj.judge.sandbox.model.ProcessRunResult;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategy;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategyImpl;
import com.huang.oj.mapper.SubmissionMapper;
import com.huang.oj.model.dto.problem.FunctionConfig;
import com.huang.oj.model.dto.problem.JudgeCases;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.model.enums.SubmissionResultEnum;
import com.huang.oj.model.enums.SubmissionStatusEnum;
import com.huang.oj.model.vo.JudgeCaseVO;
import com.huang.oj.model.vo.ProblemVO;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.SubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.huang.oj.utils.JSONUtils.parseJsonToListOfLists;
import static com.huang.oj.utils.NetUtils.RequestWithBody;

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
        //if (submissionMapper.selectCount(queryWrapperSubmission) > 1) {
        //    throw new BusinessException(ErrorCode.OPERATION_ERROR, "需等待当前判题结束");
        //} // TODO 个数判断
        Submission submission1 = new Submission();
        submission1.setId(submissionId);
        submission1.setJudgeStatus(SubmissionStatusEnum.COMPILING.getValue());
        submissionService.updateById(submission1);

        FunctionConfig functionConfig = null;
        JudgeCases judgeCases = null;
        String functionConfig1 = problem.getFunctionConfig();
        if (StringUtils.isNotBlank(functionConfig1)) {
            functionConfig = JSON.parseObject(functionConfig1, FunctionConfig.class);
        }
        String judgeCases1 = problem.getJudgeCases();
        if (StringUtils.isNotBlank(judgeCases1)) {
            judgeCases = JSON.parseObject(judgeCases1, JudgeCases.class);
        }
        String lang = submission.getLanguage().toLowerCase();
        String initCode = "";
        if (functionConfig != null) {
            initCode = functionConfig.getInitCode().get(lang);
        }
        String correctCode = "";
        if (functionConfig != null) {
            correctCode = functionConfig.getCorrectCode().get(lang);
        }
        CodeExecuteRequest codeExecuteRequest = new CodeExecuteRequest();
        codeExecuteRequest.setInitCode(initCode);
        codeExecuteRequest.setCorrectCode(correctCode);
        codeExecuteRequest.setCode(submission.getCode());
        codeExecuteRequest.setLanguage(submission.getLanguage());
        if (judgeCases != null) {
            codeExecuteRequest.setInputCases(judgeCases.getInput());
        }
        String responseStr = "";
        try {
            responseStr = RequestWithBody("http", "127.0.0.1", "8103", "codeSandbox/runProblem", codeExecuteRequest);
            ProcessRunResult processRunResult = com.alibaba.fastjson2.JSON.parseObject(responseStr, ProcessRunResult.class);
            //System.out.println(processRunResult);
            JudgeResult judgeResult = new JudgeResult();
            String[] expectedOutput = judgeCases.getExpected().split("\n");
            String[] inputs = judgeCases.getInput().split("\n");

            String runtime = processRunResult.getRuntime();
            String memoryUsed = processRunResult.getMemoryUsed();
            String stdOut = processRunResult.getStdOut();
            String funcReturn = processRunResult.getFuncReturn();

            List<Long> runtimeList = JSON.parseArray(runtime, Long.class);
            List<Long> memoryList = JSON.parseArray(runtime, Long.class);
            List<String> stdOutList = JSON.parseArray(stdOut, String.class);
            List<String> outputList = JSON.parseArray(funcReturn, String.class);

            List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
            JudgeInfo judgeInfo = new JudgeInfo();
            Long memorySum = 0L;
            for (Long value : memoryList) {
                memorySum += value;
            }
            judgeInfo.setMemoryUsed(memorySum);
            Long runtimeSum = 0L;
            for (Long aLong : runtimeList) {
                runtimeSum += aLong;
            }
            judgeInfo.setTimeUsed(runtimeSum);
            judgeInfo.setDetailCode(null);

            int i;
            for (i = 0; i < expectedOutput.length; i++) {
                JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
                judgeCaseVO.setInput(inputs[i]);
                judgeCaseVO.setStdout(stdOutList.get(i));
                judgeCaseVO.setOutput(outputList.get(i));
                judgeCaseVO.setExpected(expectedOutput[i]);
                judgeCaseVO.setRuntime(runtimeList.get(i));
                judgeCaseVO.setMemory(memoryList.get(i));
                judgeCaseVO.setResult(SubmissionResultEnum.ACCEPT.getText());
                judgeCaseVO.setDetailCode(null);
                judgeCaseVOList.add(judgeCaseVO);
                if(!Objects.equals(expectedOutput[i], outputList.get(i))) {
                    judgeCaseVO.setResult(SubmissionResultEnum.WRONG_ANSWER.getText());
                    judgeResult.setJudgeCaseVO(judgeCaseVO);
                    judgeInfo.setResultStr(SubmissionResultEnum.WRONG_ANSWER.getText());
                    break;
                }
            }
            if(judgeInfo.getResultStr() == null) {
                judgeInfo.setResultStr(SubmissionResultEnum.ACCEPT.getText());
            }
            judgeResult.setJudgeInfo(judgeInfo);
            judgeResult.setJudgeCaseVOList(judgeCaseVOList);
            judgeResult.setSubmissionId(submissionId);
            return judgeResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

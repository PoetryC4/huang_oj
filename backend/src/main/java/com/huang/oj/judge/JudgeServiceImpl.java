package com.huang.oj.judge;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.judge.sandbox.model.CodeExecuteRequest;
import com.huang.oj.judge.sandbox.model.JudgeResult;
import com.huang.oj.judge.sandbox.model.ProcessRunResult;
import com.huang.oj.judge.sandbox.strategy.JudgeStrategy;
import com.huang.oj.mapper.SubmissionMapper;
import com.huang.oj.model.dto.problem.FunctionConfig;
import com.huang.oj.model.dto.problem.JudgeCases;
import com.huang.oj.model.dto.problem.JudgeConfig;
import com.huang.oj.model.dto.submission.JudgeInfo;
import com.huang.oj.model.dto.submission.ProblemSubmitQuest;
import com.huang.oj.model.dto.submission.ProblemTestExampleRequest;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.model.enums.SubmissionResultEnum;
import com.huang.oj.model.vo.JudgeCaseVO;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.SubmissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.huang.oj.utils.NetUtils.RequestWithBody;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private SubmissionService submissionService;

    @Resource
    private ProblemService problemService;

    @Resource
    private SubmissionMapper submissionMapper;

    @Resource
    private JudgeStrategy judgeStrategy;

    @Value("${sandbox.type}")
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
        queryWrapperSubmission.eq("judgeStatus", SubmissionResultEnum.JUDGING.getValue()).or().eq("judgeStatus", SubmissionResultEnum.COMPILING.getValue()).or().eq("judgeStatus", SubmissionResultEnum.RUNNING.getValue()).or().eq("judgeStatus", SubmissionResultEnum.SUBMITTING.getValue());
        //if (submissionMapper.selectCount(queryWrapperSubmission) > 1) {
        //    throw new BusinessException(ErrorCode.OPERATION_ERROR, "需等待当前判题结束");
        //} // TODO 个数判断
        Submission submission1 = new Submission();
        submission1.setId(submissionId);
        submission1.setJudgeStatus(SubmissionResultEnum.COMPILING.getValue());
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
        codeExecuteRequest.setFunctionConfig(functionConfig);
        codeExecuteRequest.setCode(submission.getCode());
        codeExecuteRequest.setLanguage(submission.getLanguage());
        codeExecuteRequest.setFunctionConfig(JSON.parseObject(problem.getFunctionConfig(), FunctionConfig.class));
        if (judgeCases != null) {
            codeExecuteRequest.setInputCases(judgeCases.getInput());
        }
        JudgeConfig judgeConfig = JSON.parseObject(problem.getJudgeConfig(), JudgeConfig.class);
        String responseStr = "";
        try {
            responseStr = RequestWithBody("http", "192.168.137.128", "8103", "codeSandbox/runProblem", codeExecuteRequest);
            ProcessRunResult processRunResult = com.alibaba.fastjson2.JSON.parseObject(responseStr, ProcessRunResult.class);
            if (processRunResult.getErrMessage() != null) {
                JudgeResult judgeResult = new JudgeResult();
                JudgeInfo judgeInfo = new JudgeInfo();
                judgeInfo.setResultStr(processRunResult.getErrMessage());
                judgeResult.setSubmissionId(submissionId);
                judgeResult.setJudgeInfo(judgeInfo);
                submission1.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
                submission1.setJudgeStatus(SubmissionResultEnum.COMPILE_ERROR.getValue());
                submissionService.updateById(submission1);
                return judgeResult;
            }
            JudgeResult judgeResult = new JudgeResult();
            String[] expectedOutput = judgeCases.getExpected().split("\n");
            String[] inputs = judgeCases.getInput().split("\n");
            List<String> expectedOutputList = Arrays.asList(expectedOutput);
            expectedOutputList.replaceAll(String::trim);

            List<Long> runtimeList = processRunResult.getRuntime();
            List<Long> memoryList = processRunResult.getMemoryUsed();
            List<String> stdOutList = processRunResult.getStdOut();
            List<String> outputList = processRunResult.getFuncReturn();
            outputList.replaceAll(String::trim);
            String errMessage = processRunResult.getErrMessage();

            JudgeInfo judgeInfo = new JudgeInfo();
            Long memorySum = 0L;
            Long runtimeSum = 0L;
            judgeInfo.setTimeUsed(runtimeSum);
            judgeInfo.setDetailCode(null);

            int i;
            for (i = 0; i < outputList.size(); i++) {

                memorySum += memoryList.get(i);
                runtimeSum += runtimeList.get(i);
                String judgeRes = SubmissionResultEnum.getEnumByValue(judgeStrategy.judgeLimit(submission.getLanguage(), runtimeSum, memorySum, judgeConfig)).getText();
                // 检测空间与时间使用
                if (!Objects.equals(judgeRes, SubmissionResultEnum.ACCEPT.getText())) {
                    if (Objects.equals(judgeRes, SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getText())) {
                        submission1.setJudgeStatus(SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getValue());
                    }
                    if (Objects.equals(judgeRes, SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getText())) {
                        submission1.setJudgeStatus(SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
                    }
                    submissionService.updateById(submission1);

                    JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
                    judgeCaseVO.setStdout(stdOutList.get(i));
                    judgeCaseVO.setOutput(outputList.get(i));
                    StringBuilder inputTmp = new StringBuilder();
                    for (int j = 0; j < functionConfig.getVarCount(); j++) {
                        inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                        inputTmp.append("\n");
                    }
                    judgeCaseVO.setInput(inputTmp.toString());
                    judgeCaseVO.setExpected(expectedOutputList.get(i));
                    judgeCaseVO.setRuntime(runtimeList.get(i));
                    judgeCaseVO.setMemory(memoryList.get(i));
                    judgeCaseVO.setDetailCode(null);
                    judgeCaseVO.setResult(judgeRes);
                    judgeResult.setJudgeCaseVO(judgeCaseVO);
                    judgeInfo.setResultStr(judgeRes);
                    List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
                    judgeCaseVOList.add(judgeCaseVO);
                    judgeResult.setJudgeCaseVOList(judgeCaseVOList);
                    break;
                }
                if (!Objects.equals(expectedOutputList.get(i), outputList.get(i))) {//放入错误样例
                    submission1.setJudgeStatus(SubmissionResultEnum.WRONG_ANSWER.getValue());
                    submissionService.updateById(submission1);

                    JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
                    judgeCaseVO.setStdout(stdOutList.get(i));
                    judgeCaseVO.setOutput(outputList.get(i));
                    StringBuilder inputTmp = new StringBuilder();
                    for (int j = 0; j < functionConfig.getVarCount(); j++) {
                        inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                        inputTmp.append("\n");
                    }
                    judgeCaseVO.setInput(inputTmp.toString());
                    judgeCaseVO.setExpected(expectedOutputList.get(i));
                    judgeCaseVO.setRuntime(runtimeList.get(i));
                    judgeCaseVO.setMemory(memoryList.get(i));
                    judgeCaseVO.setDetailCode(null);
                    judgeCaseVO.setResult(SubmissionResultEnum.WRONG_ANSWER.getText());
                    judgeResult.setJudgeCaseVO(judgeCaseVO);
                    judgeInfo.setResultStr(SubmissionResultEnum.WRONG_ANSWER.getText());
                    List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
                    judgeCaseVOList.add(judgeCaseVO);
                    judgeResult.setJudgeCaseVOList(judgeCaseVOList);
                    break;
                }
            }
            if (judgeInfo.getResultStr() == null) {
                judgeInfo.setResultStr(SubmissionResultEnum.ACCEPT.getText());
                submission1.setJudgeStatus(SubmissionResultEnum.ACCEPT.getValue());
                judgeInfo.setMemoryUsed(memorySum);
                judgeInfo.setTimeUsed(runtimeSum);
                judgeResult.setSubmissionId(submissionId);
                judgeResult.setJudgeInfo(judgeInfo);
                submission1.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
                submissionService.updateById(submission1);
            } else {
                submission1.setJudgeStatus(SubmissionResultEnum.WRONG_ANSWER.getValue());
                judgeInfo.setMemoryUsed(memorySum);
                judgeInfo.setTimeUsed(runtimeSum);
                judgeResult.setSubmissionId(submissionId);
                judgeResult.setJudgeInfo(judgeInfo);
                submission1.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
                submissionService.updateById(submission1);
            }
            return judgeResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JudgeResult testJudge(ProblemTestExampleRequest problemTestExampleRequest) {
        ProblemSubmitQuest problemSubmitQuest = problemTestExampleRequest.getProblemSubmitQuest();
        Problem problem = problemService.getById(problemSubmitQuest.getProblemId());
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        FunctionConfig functionConfig = null;
        String functionConfig1 = problem.getFunctionConfig();
        if (StringUtils.isNotBlank(functionConfig1)) {
            functionConfig = JSON.parseObject(functionConfig1, FunctionConfig.class);
        }
        String lang = problemSubmitQuest.getLanguage().toLowerCase();
        String initCode = "";
        if (functionConfig != null) {
            initCode = functionConfig.getInitCode().get(lang);
        }
        String correctCode = "";
        if (functionConfig != null) {
            correctCode = functionConfig.getCorrectCode().get(lang);
        }
        CodeExecuteRequest codeExecuteRequest = new CodeExecuteRequest();
        codeExecuteRequest.setFunctionConfig(functionConfig);
        codeExecuteRequest.setLanguage(problemSubmitQuest.getLanguage());
        codeExecuteRequest.setInputCases(problemTestExampleRequest.getJudgeCases().getInput());
        codeExecuteRequest.setFunctionConfig(JSON.parseObject(problem.getFunctionConfig(), FunctionConfig.class));
        JudgeConfig judgeConfig = JSON.parseObject(problem.getJudgeConfig(), JudgeConfig.class);

        codeExecuteRequest.setCode(correctCode);
        String responseStr1 = "";
        List<String> expectedOutputs;
        try {
            responseStr1 = RequestWithBody("http", "192.168.137.128", "8103", "codeSandbox/runProblem", codeExecuteRequest);
            ProcessRunResult processRunResult = com.alibaba.fastjson2.JSON.parseObject(responseStr1, ProcessRunResult.class);
            if (processRunResult.getErrMessage() != null) {
                JudgeResult judgeResult = new JudgeResult();
                JudgeInfo judgeInfo = new JudgeInfo();
                judgeInfo.setResultStr(processRunResult.getErrMessage());
                judgeResult.setJudgeInfo(judgeInfo);
                return judgeResult;
            }
            expectedOutputs = processRunResult.getFuncReturn();
            //去除前置后置换行符
            expectedOutputs.replaceAll(String::trim);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        codeExecuteRequest.setCode(problemSubmitQuest.getCode());

        String responseStr2 = "";
        try {
            responseStr2 = RequestWithBody("http", "192.168.137.128", "8103", "codeSandbox/runProblem", codeExecuteRequest);
            ProcessRunResult processRunResult = com.alibaba.fastjson2.JSON.parseObject(responseStr2, ProcessRunResult.class);
            if (processRunResult.getErrMessage() != null) {
                JudgeResult judgeResult = new JudgeResult();
                JudgeInfo judgeInfo = new JudgeInfo();
                judgeInfo.setResultStr(processRunResult.getErrMessage());
                judgeResult.setSubmissionId(null);
                judgeResult.setJudgeInfo(judgeInfo);
                return judgeResult;
            }
            //System.out.println(processRunResult);
            JudgeResult judgeResult = new JudgeResult();
            String[] inputs = problemTestExampleRequest.getJudgeCases().getInput().split("\n");

            List<Long> runtimeList = processRunResult.getRuntime();
            List<Long> memoryList = processRunResult.getMemoryUsed();
            List<String> stdOutList = processRunResult.getStdOut();
            List<String> outputList = processRunResult.getFuncReturn();
            //去除前置后置换行符
            outputList.replaceAll(String::trim);
            String errMessage = processRunResult.getErrMessage();

            JudgeInfo judgeInfo = new JudgeInfo();
            Long memorySum = 0L;
            Long runtimeSum = 0L;
            judgeInfo.setTimeUsed(runtimeSum);
            judgeInfo.setDetailCode(null);

            int i;
            List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
            for (i = 0; i < expectedOutputs.size(); i++) {
                JudgeCaseVO judgeCaseVO = new JudgeCaseVO();
                judgeCaseVO.setStdout(stdOutList.get(i));
                judgeCaseVO.setOutput(outputList.get(i));
                StringBuilder inputTmp = new StringBuilder();
                for (int j = 0; j < functionConfig.getVarCount(); j++) {
                    inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                    inputTmp.append("\n");
                }
                judgeCaseVO.setInput(inputTmp.toString());
                judgeCaseVO.setExpected(expectedOutputs.get(i));
                judgeCaseVO.setRuntime(runtimeList.get(i));
                judgeCaseVO.setMemory(memoryList.get(i));
                judgeCaseVO.setDetailCode(null);
                judgeCaseVO.setResult(SubmissionResultEnum.ACCEPT.getText());
                judgeCaseVOList.add(judgeCaseVO);

                memorySum += memoryList.get(i);
                runtimeSum += runtimeList.get(i);
                String judgeRes = SubmissionResultEnum.getEnumByValue(judgeStrategy.judgeLimit(problemSubmitQuest.getLanguage(), runtimeSum, memorySum, judgeConfig)).getText();
                // 检测空间与时间使用
                if (!Objects.equals(judgeRes, SubmissionResultEnum.ACCEPT.getText())) {
                    judgeCaseVO.setResult(judgeRes);
                    judgeResult.setJudgeCaseVO(judgeCaseVO);
                    judgeInfo.setResultStr(judgeRes);
                    break;
                }
                if (!Objects.equals(expectedOutputs.get(i), outputList.get(i))) {//放入错误样例
                    judgeCaseVO.setResult(SubmissionResultEnum.WRONG_ANSWER.getText());
                    judgeResult.setJudgeCaseVO(judgeCaseVO);
                    judgeInfo.setResultStr(SubmissionResultEnum.WRONG_ANSWER.getText());
                    break;
                }
            }
            judgeInfo.setMemoryUsed(memorySum);
            judgeInfo.setTimeUsed(runtimeSum);
            judgeResult.setJudgeCaseVOList(judgeCaseVOList);
            if (judgeInfo.getResultStr() == null) {
                judgeInfo.setResultStr(SubmissionResultEnum.ACCEPT.getText());
            }
            judgeResult.setJudgeInfo(judgeInfo);
            judgeResult.setSubmissionId(null);
            return judgeResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

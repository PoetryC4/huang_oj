package com.yioj.submissionservice.judge;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yioj.clientservice.service.ProblemFeignClient;
import com.yioj.common.common.ErrorCode;
import com.yioj.common.exception.BusinessException;
import com.yioj.common.utils.EncryptionUtils;
import com.yioj.model.model.dto.problem.FunctionConfig;
import com.yioj.model.model.dto.problem.JudgeCases;
import com.yioj.model.model.dto.problem.JudgeConfig;
import com.yioj.model.model.dto.submission.JudgeInfo;
import com.yioj.model.model.dto.submission.ProblemSubmitQuest;
import com.yioj.model.model.dto.submission.ProblemTestExampleRequest;
import com.yioj.model.model.entity.Problem;
import com.yioj.model.model.entity.Submission;
import com.yioj.model.model.enums.SubmissionResultEnum;
import com.yioj.model.model.judge.CodeExecuteRequest;
import com.yioj.model.model.judge.JudgeResult;
import com.yioj.model.model.judge.ProcessRunResult;
import com.yioj.model.model.vo.JudgeCaseVO;
import com.yioj.submissionservice.judge.sandbox.strategy.JudgeStrategy;
import com.yioj.submissionservice.mapper.SubmissionMapper;
import com.yioj.submissionservice.service.SubmissionService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    private String accessKey = "KS8d794nxMf4o53gDSepIlxhJYwa3hpx";
    private String secretKey = "LOzTZrd4KjfvnSw26c8HLBqlTSkTHW9A5rmOYzEfvX2EU73ykb5rxQFR06oPnnrt";

    @Value("${sandbox.remote-url}")
    private String codeSandboxUrl;

    @Value("${spring.application.name}")
    private String appName;
    @Resource
    private SubmissionService submissionService;

    @Resource
    private ProblemFeignClient problemService;

    @Resource
    private SubmissionMapper submissionMapper;

    @Resource
    private JudgeStrategy judgeStrategy;
    /*@Resource
    private CircuitBreaker circuitBreaker;*/

    @Value("${sandbox.type}")
    private String type;

    /**
     * 请求代码沙箱
     *
     * @param body
     * @return
     */
    /*public ProcessRunResult requestCodeSandbox(Object body) {
        try {
            return circuitBreaker.executeSupplier(() -> {
                long timestamp = new Date().getTime();
                String signature = EncryptionUtils.generateSignature(appName + timestamp, timestamp, secretKey);
                System.out.println("签证:"+signature);
                String responseStr1 = HttpUtil.createPost(codeSandboxUrl)
                        .header("Content-Type", "application/json")
                        .header("AccessKey", accessKey)
                        .header("Timestamp", String.valueOf(timestamp))
                        .header("Code", appName)
                        .header("Signature", signature)
                        .body(JSON.toJSONString(body))
                        .execute()
                        .body();
                if (responseStr1.contains("无权限")) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
                if (StringUtils.isBlank(responseStr1)) {
                    throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
                }
                return JSON.parseObject(responseStr1, ProcessRunResult.class);
            });
        } catch (CallNotPermittedException ex) {
            // 处理熔断时的逻辑，例如返回一个默认值或者记录日志
            throw new BusinessException(ErrorCode.API_REQUEST_FAILED, "熔断");
        }
    }*/
    public ProcessRunResult requestCodeSandbox(Object body) {
        long timestamp = new Date().getTime();
        String signature = EncryptionUtils.generateSignature(appName + timestamp, timestamp, secretKey);
        System.out.println("签证:"+signature);
        String responseStr1 = HttpUtil.createPost(codeSandboxUrl)
                .header("Content-Type", "application/json")
                .header("AccessKey", accessKey)
                .header("Timestamp", String.valueOf(timestamp))
                .header("Code", appName)
                .header("Signature", signature)
                .body(JSON.toJSONString(body))
                .execute()
                .body();
        if (responseStr1.contains("无权限")) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (StringUtils.isBlank(responseStr1)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR);
        }
        return JSON.parseObject(responseStr1, ProcessRunResult.class);
    }
    @Override
    @CircuitBreaker(name = "submissionCircuitBreaker", fallbackMethod = "doFallbackMethod")
    @RateLimiter(name = "submissionRateLimiter")
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
        Submission submissionUpdate = new Submission();
        submissionUpdate.setId(submissionId);
        submissionUpdate.setJudgeStatus(SubmissionResultEnum.COMPILING.getValue());
        submissionService.updateById(submissionUpdate);

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

        CodeExecuteRequest codeExecuteRequest = new CodeExecuteRequest();
        codeExecuteRequest.setFunctionConfig(functionConfig);
        codeExecuteRequest.setCode(submission.getCode());
        codeExecuteRequest.setLanguage(submission.getLanguage());
        codeExecuteRequest.setFunctionConfig(JSON.parseObject(problem.getFunctionConfig(), FunctionConfig.class));
        if (judgeCases != null) {
            codeExecuteRequest.setInputCases(judgeCases.getInput());
        }
        JudgeConfig judgeConfig = JSON.parseObject(problem.getJudgeConfig(), JudgeConfig.class);
        ProcessRunResult processRunResult1 = requestCodeSandbox(codeExecuteRequest);
        if (processRunResult1.getErrMessage() != null) {
            JudgeResult judgeResult = new JudgeResult();
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setResultStr(processRunResult1.getErrMessage());
            judgeResult.setSubmissionId(submissionId);
            judgeResult.setJudgeInfo(judgeInfo);
            submissionUpdate.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
            submissionUpdate.setJudgeStatus(SubmissionResultEnum.COMPILE_ERROR.getValue());
            submissionService.updateById(submissionUpdate);
            return judgeResult;
        }
        JudgeResult judgeResult = new JudgeResult();
        String[] expectedOutput = judgeCases.getExpected().split("\n");
        String[] inputs = judgeCases.getInput().split("\n");
        List<String> expectedOutputList = Arrays.asList(expectedOutput);
        expectedOutputList.replaceAll(String::trim);

        List<Long> runtimeList = processRunResult1.getRuntime();
        List<Long> memoryList = processRunResult1.getMemoryUsed();
        List<String> stdOutList = processRunResult1.getStdOut();
        List<String> outputList = processRunResult1.getFuncReturn();
        outputList.replaceAll(String::trim);

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
                    submissionUpdate.setJudgeStatus(SubmissionResultEnum.TIME_LIMIT_EXCEEDED.getValue());
                }
                if (Objects.equals(judgeRes, SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getText())) {
                    submissionUpdate.setJudgeStatus(SubmissionResultEnum.MEMORY_LIMIT_EXCEEDED.getValue());
                }
                submissionService.updateById(submissionUpdate);

                StringBuilder inputTmp = new StringBuilder();
                for (int j = 0; j < functionConfig.getVarCount(); j++) {
                    inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                    inputTmp.append("\n");
                }

                JudgeCaseVO judgeCaseVO = JudgeCaseVO.builder()
                        .input(inputTmp.toString())
                        .stdout(stdOutList.get(i))
                        .output(outputList.get(i))
                        .expected(expectedOutputList.get(i))
                        .runtime(runtimeList.get(i))
                        .memory(memoryList.get(i))
                        .detailCode(null)
                        .result(judgeRes)
                        .build();
                judgeResult.setJudgeCaseVO(judgeCaseVO);
                judgeInfo.setResultStr(judgeRes);
                List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
                judgeCaseVOList.add(judgeCaseVO);
                judgeResult.setJudgeCaseVOList(judgeCaseVOList);
                break;
            }
            if (!Objects.equals(expectedOutputList.get(i), outputList.get(i))) {//放入错误样例
                submissionUpdate.setJudgeStatus(SubmissionResultEnum.WRONG_ANSWER.getValue());
                submissionService.updateById(submissionUpdate);

                StringBuilder inputTmp = new StringBuilder();
                for (int j = 0; j < functionConfig.getVarCount(); j++) {
                    inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                    inputTmp.append("\n");
                }

                JudgeCaseVO judgeCaseVO = JudgeCaseVO
                        .builder()
                        .stdout(stdOutList.get(i))
                        .output(outputList.get(i))
                        .input(inputTmp.toString())
                        .expected(expectedOutputList.get(i))
                        .runtime(runtimeList.get(i))
                        .memory(memoryList.get(i))
                        .detailCode(null)
                        .result(SubmissionResultEnum.WRONG_ANSWER.getText())
                        .build();
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
            submissionUpdate.setJudgeStatus(SubmissionResultEnum.ACCEPT.getValue());
            judgeInfo.setMemoryUsed(memorySum);
            judgeInfo.setTimeUsed(runtimeSum);
            judgeResult.setSubmissionId(submissionId);
            judgeResult.setJudgeInfo(judgeInfo);
            submissionUpdate.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
            submissionService.updateById(submissionUpdate);
        } else {
            submissionUpdate.setJudgeStatus(SubmissionResultEnum.WRONG_ANSWER.getValue());
            judgeInfo.setMemoryUsed(memorySum);
            judgeInfo.setTimeUsed(runtimeSum);
            judgeResult.setSubmissionId(submissionId);
            judgeResult.setJudgeInfo(judgeInfo);
            submissionUpdate.setJudgeResult(com.alibaba.fastjson2.JSON.toJSONString(judgeResult));
            submissionService.updateById(submissionUpdate);
        }
        return judgeResult;
    }

    @Override
    @CircuitBreaker(name = "submissionCircuitBreaker", fallbackMethod = "testFallbackMethod")
    @RateLimiter(name = "submissionRateLimiter")
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
        List<String> expectedOutputs;
        ProcessRunResult processRunResult1 = requestCodeSandbox(codeExecuteRequest);

        if (processRunResult1.getErrMessage() != null) {
            JudgeResult judgeResult = new JudgeResult();
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setResultStr(processRunResult1.getErrMessage());
            judgeResult.setJudgeInfo(judgeInfo);
            return judgeResult;
        }
        expectedOutputs = processRunResult1.getFuncReturn();
        //去除前置后置换行符
        expectedOutputs.replaceAll(String::trim);

        codeExecuteRequest.setCode(problemSubmitQuest.getCode());

        ProcessRunResult processRunResult2 = requestCodeSandbox(codeExecuteRequest);
        if (processRunResult2.getErrMessage() != null) {
            JudgeResult judgeResult = new JudgeResult();
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setResultStr(processRunResult2.getErrMessage());
            judgeResult.setJudgeInfo(judgeInfo);
            return judgeResult;
        }
        //System.out.println(processRunResult);
        JudgeResult judgeResult = new JudgeResult();
        String[] inputs = problemTestExampleRequest.getJudgeCases().getInput().split("\n");

        List<Long> runtimeList = processRunResult2.getRuntime();
        List<Long> memoryList = processRunResult2.getMemoryUsed();
        List<String> stdOutList = processRunResult2.getStdOut();
        List<String> outputList = processRunResult2.getFuncReturn();
        //去除前置后置换行符
        outputList.replaceAll(String::trim);
        String errMessage = processRunResult2.getErrMessage();

        JudgeInfo judgeInfo = new JudgeInfo();
        Long memorySum = 0L;
        Long runtimeSum = 0L;
        judgeInfo.setTimeUsed(runtimeSum);
        judgeInfo.setDetailCode(null);

        int i;
        List<JudgeCaseVO> judgeCaseVOList = new ArrayList<>();
        for (i = 0; i < expectedOutputs.size(); i++) {
            StringBuilder inputTmp = new StringBuilder();
            for (int j = 0; j < functionConfig.getVarCount(); j++) {
                inputTmp.append(inputs[i * functionConfig.getVarCount() + j]);
                inputTmp.append("\n");
            }

            JudgeCaseVO judgeCaseVO = JudgeCaseVO
                    .builder()
                    .stdout(stdOutList.get(i))
                    .output(outputList.get(i))
                    .input(inputTmp.toString())
                    .expected(expectedOutputs.get(i))
                    .runtime(runtimeList.get(i))
                    .memory(memoryList.get(i))
                    .detailCode(null)
                    .result(SubmissionResultEnum.ACCEPT.getText())
                    .build();
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
    }
    // 熔断触发函数
    public JudgeResult testFallbackMethod(ProblemTestExampleRequest problemTestExampleRequest, Throwable t) {
        // 降级逻辑，例如返回一个默认值
        t.printStackTrace();
        log.error("熔断, 请求信息:"+problemTestExampleRequest.toString()+"   错误信息:"+t.getMessage());
        return null;
    }
    public JudgeResult doFallbackMethod(long submissionId, Throwable t) {
        // 降级逻辑，例如返回一个默认值
        t.printStackTrace();
        log.error("熔断, 请求id:"+submissionId+"   错误信息:"+t.getMessage());
        return null;
    }
}

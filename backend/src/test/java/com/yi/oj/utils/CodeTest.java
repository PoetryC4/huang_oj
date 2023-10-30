package com.yi.oj.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.yi.oj.judge.sandbox.model.CodeExecuteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CodeTest {
    @Value("${sandbox.remote-url}")
    private String codeSandboxUrl;

    @Test
    public void test() throws Exception {
        String responseStr1 = HttpUtil.createPost(codeSandboxUrl)
                .header("Content-Type", "application/json")
                .header("AccessKey", "23423")
                .header("Timestamp", String.valueOf(System.currentTimeMillis()))
                .header("Signature", "ushdvch")
                .body(JSON.toJSONString(CodeExecuteRequest.builder().code("test").build()))
                .execute()
                .body();
        System.out.println(responseStr1);
    }
}
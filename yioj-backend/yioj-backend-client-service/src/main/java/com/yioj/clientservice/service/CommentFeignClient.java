package com.yioj.clientservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yioj-backend-comment-service", path = "/api/comment/inner")
public interface CommentFeignClient {
    @PostMapping("/email/send")
    void sendVerificationCode(@RequestParam("toEmail") String toEmail);
}

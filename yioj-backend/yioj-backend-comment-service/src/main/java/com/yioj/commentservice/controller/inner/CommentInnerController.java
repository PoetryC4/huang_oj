package com.yioj.commentservice.controller.inner;


import com.yioj.clientservice.service.CommentFeignClient;
import com.yioj.commentservice.service.EmailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class CommentInnerController implements CommentFeignClient {
    @Resource
    private EmailService emailService;

    @PostMapping("/email/send")
    public void sendVerificationCode(@RequestParam("toEmail") String toEmail) {
        emailService.sendVerificationCode(toEmail);
    }
}

package com.huang.oj.controller;

import com.huang.oj.common.BaseResponse;
import com.huang.oj.common.ErrorCode;
import com.huang.oj.common.ResultUtils;
import com.huang.oj.exception.BusinessException;
import com.huang.oj.model.dto.postthumb.PostThumbAddRequest;
import com.huang.oj.model.entity.User;
import com.huang.oj.service.SubmissionService;
import com.huang.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/submission")
@Slf4j
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        //int result = submissionService.doPostThumb(postId, loginUser);
        //return ResultUtils.success(result);
        return null;
    }

}

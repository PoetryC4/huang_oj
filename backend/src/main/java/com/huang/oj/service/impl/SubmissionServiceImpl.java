package com.huang.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.oj.model.entity.Submission;
import com.huang.oj.service.SubmissionService;
import com.huang.oj.mapper.SubmissionMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【submission(提交)】的数据库操作Service实现
* @createDate 2023-09-26 21:03:14
*/
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission>
    implements SubmissionService{

}





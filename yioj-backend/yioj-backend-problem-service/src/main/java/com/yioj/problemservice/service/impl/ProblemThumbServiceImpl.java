package com.yioj.problemservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yioj.model.model.entity.ProblemThumb;
import com.yioj.problemservice.mapper.ProblemThumbMapper;
import com.yioj.problemservice.service.ProblemThumbService;
import org.springframework.stereotype.Service;

@Service
public class ProblemThumbServiceImpl extends ServiceImpl<ProblemThumbMapper, ProblemThumb>
        implements ProblemThumbService {
}

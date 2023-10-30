package com.yi.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yi.oj.mapper.ProblemThumbMapper;
import com.yi.oj.model.entity.ProblemThumb;
import com.yi.oj.service.ProblemThumbService;
import org.springframework.stereotype.Service;

@Service
public class ProblemThumbServiceImpl extends ServiceImpl<ProblemThumbMapper, ProblemThumb>
        implements ProblemThumbService {
}

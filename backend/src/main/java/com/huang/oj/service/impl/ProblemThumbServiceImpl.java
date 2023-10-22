package com.huang.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.oj.mapper.ProblemMapper;
import com.huang.oj.mapper.ProblemThumbMapper;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.model.entity.ProblemThumb;
import com.huang.oj.service.ProblemService;
import com.huang.oj.service.ProblemThumbService;
import org.springframework.stereotype.Service;

@Service
public class ProblemThumbServiceImpl extends ServiceImpl<ProblemThumbMapper, ProblemThumb>
        implements ProblemThumbService {
}

package com.yioj.problemservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yioj.model.model.entity.ProblemDislike;
import com.yioj.problemservice.mapper.ProblemDislikeMapper;
import com.yioj.problemservice.service.ProblemDislikeService;
import org.springframework.stereotype.Service;

@Service
public class ProblemDislikeServiceImpl extends ServiceImpl<ProblemDislikeMapper, ProblemDislike>
        implements ProblemDislikeService {
}

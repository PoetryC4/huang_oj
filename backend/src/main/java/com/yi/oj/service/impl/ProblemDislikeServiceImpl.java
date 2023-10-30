package com.yi.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yi.oj.mapper.ProblemDislikeMapper;
import com.yi.oj.model.entity.ProblemDislike;
import com.yi.oj.service.ProblemDislikeService;
import org.springframework.stereotype.Service;

@Service
public class ProblemDislikeServiceImpl extends ServiceImpl<ProblemDislikeMapper, ProblemDislike>
        implements ProblemDislikeService {
}

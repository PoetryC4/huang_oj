package com.huang.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.oj.mapper.ProblemDislikeMapper;
import com.huang.oj.mapper.ProblemThumbMapper;
import com.huang.oj.model.entity.ProblemDislike;
import com.huang.oj.model.entity.ProblemThumb;
import com.huang.oj.service.ProblemDislikeService;
import com.huang.oj.service.ProblemThumbService;
import org.springframework.stereotype.Service;

@Service
public class ProblemDislikeServiceImpl extends ServiceImpl<ProblemDislikeMapper, ProblemDislike>
        implements ProblemDislikeService {
}

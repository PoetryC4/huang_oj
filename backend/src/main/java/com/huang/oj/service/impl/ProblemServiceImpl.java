package com.huang.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.oj.model.entity.Problem;
import com.huang.oj.service.ProblemService;
import com.huang.oj.mapper.ProblemMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【problem(题)】的数据库操作Service实现
* @createDate 2023-09-26 22:13:18
*/
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem>
    implements ProblemService{

}





package com.huang.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.oj.mapper.CommentThumbMapper;
import com.huang.oj.model.entity.CommentThumb;
import com.huang.oj.service.CommentThumbService;
import org.springframework.stereotype.Service;

@Service
public class CommentThumbServiceImpl extends ServiceImpl<CommentThumbMapper, CommentThumb> implements CommentThumbService {
}

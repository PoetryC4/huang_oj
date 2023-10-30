package com.yi.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yi.oj.mapper.CommentThumbMapper;
import com.yi.oj.model.entity.CommentThumb;
import com.yi.oj.service.CommentThumbService;
import org.springframework.stereotype.Service;

@Service
public class CommentThumbServiceImpl extends ServiceImpl<CommentThumbMapper, CommentThumb> implements CommentThumbService {
}

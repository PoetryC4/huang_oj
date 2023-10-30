package com.yioj.commentservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yioj.commentservice.mapper.CommentThumbMapper;
import com.yioj.commentservice.service.CommentThumbService;
import com.yioj.model.model.entity.CommentThumb;
import org.springframework.stereotype.Service;

@Service
public class CommentThumbServiceImpl extends ServiceImpl<CommentThumbMapper, CommentThumb> implements CommentThumbService {
}

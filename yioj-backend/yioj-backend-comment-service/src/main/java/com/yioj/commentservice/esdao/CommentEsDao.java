package com.yioj.commentservice.esdao;


import com.yioj.model.model.dto.comment.CommentEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CommentEsDao extends ElasticsearchRepository<CommentEsDTO, Long> {

    List<CommentEsDTO> findByUserId(Long userId);
}
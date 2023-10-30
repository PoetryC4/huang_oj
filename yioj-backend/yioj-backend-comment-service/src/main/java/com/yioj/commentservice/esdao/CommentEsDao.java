package com.yioj.commentservice.esdao;


import com.yioj.model.model.dto.comment.CommentEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface CommentEsDao extends ElasticsearchRepository<CommentEsDTO, Long> {

    List<CommentEsDTO> findByUserId(Long userId);
}
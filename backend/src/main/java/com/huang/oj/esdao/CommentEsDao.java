package com.huang.oj.esdao;

import com.huang.oj.model.dto.comment.CommentEsDTO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface CommentEsDao extends ElasticsearchRepository<CommentEsDTO, Long> {

    List<CommentEsDTO> findByUserId(Long userId);
}
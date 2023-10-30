package com.yi.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.yi.oj.common.ErrorCode;
import com.yi.oj.constant.CommonConstant;
import com.yi.oj.exception.BusinessException;
import com.yi.oj.exception.ThrowUtils;
import com.yi.oj.mapper.CommentMapper;
import com.yi.oj.mapper.CommentThumbMapper;
import com.yi.oj.mapper.ProblemMapper;
import com.yi.oj.model.dto.comment.CommentEsDTO;
import com.yi.oj.model.dto.comment.CommentQueryRequest;
import com.yi.oj.model.entity.*;
import com.yi.oj.model.entity.Comment;
import com.yi.oj.model.entity.CommentThumb;
import com.yi.oj.model.entity.Problem;
import com.yi.oj.model.entity.User;
import com.yi.oj.model.vo.CommentVO;
import com.yi.oj.model.vo.ProblemVO;
import com.yi.oj.model.vo.UserVO;
import com.yi.oj.service.CommentService;
import com.yi.oj.service.CommentThumbService;
import com.yi.oj.service.ProblemService;
import com.yi.oj.service.UserService;
import com.yi.oj.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    @Resource
    private CommentThumbMapper commentThumbMapper;
    @Resource
    private CommentThumbService commentThumbService;
    @Resource
    private ProblemService problemService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ProblemMapper problemMapper;

    @Override
    public void validComment(Comment comment, boolean add) {
        if (comment == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long problemId = comment.getProblemId();
        Problem problem = problemMapper.selectById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String content = comment.getContent();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(content), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param commentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = commentQueryRequest.getSearchText();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        Long problemId = commentQueryRequest.getProblemId();

        Long id = commentQueryRequest.getId();
        Long userId = commentQueryRequest.getUserId();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("content", searchText);
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        queryWrapper.orderByDesc("createTime");
        return queryWrapper;
    }

    @Override
    public CommentVO getCommentVO(Comment comment, HttpServletRequest request) {
        CommentVO commentVO = CommentVO.objToVo(comment);
        // 1. 获取用户，获取问题
        Long userId = comment.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        commentVO.setUserVO(userVO);

        Long problemId = comment.getProblemId();
        Problem problem = null;
        if (problemId != null && problemId > 0) {
            problem = problemService.getById(problemId);
        }
        ProblemVO problemVO = ProblemVO.objToVo(problem);
        commentVO.setProblemVO(problemVO);

        // 2. 已登录，获取用户点赞、收藏状态
        long commentId = comment.getId();
        commentVO.setIsLiked(false);
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
            QueryWrapper<CommentThumb> commentThumbQueryWrapper = new QueryWrapper<>();
            commentThumbQueryWrapper.in("commentId", commentId);
            commentThumbQueryWrapper.eq("userId", loginUser.getId());
            CommentThumb commentThumb = commentThumbMapper.selectOne(commentThumbQueryWrapper);
            commentVO.setIsLiked(commentThumb != null);
        }
        return commentVO;
    }

    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request) {
        List<Comment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollectionUtils.isEmpty(commentList)) {
            return commentVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = commentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> commentIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> commentIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> commentIdSet = commentList.stream().map(Comment::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<CommentThumb> commentThumbQueryWrapper = new QueryWrapper<>();
            commentThumbQueryWrapper.in("commentId", commentIdSet);
            commentThumbQueryWrapper.eq("userId", loginUser.getId());
            List<CommentThumb> commentCommentThumbList = commentThumbMapper.selectList(commentThumbQueryWrapper);
            commentCommentThumbList.forEach(commentCommentThumb -> commentIdHasThumbMap.put(commentCommentThumb.getCommentId(), true));
        }
        // 填充信息
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            CommentVO commentVO = CommentVO.objToVo(comment);

            Long userId = comment.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                user = userService.getById(userId);
            }
            UserVO userVO = userService.getUserVO(user);
            commentVO.setUserVO(userVO);

            Long problemId = comment.getProblemId();
            Problem problem = null;
            if (problemId != null && problemId > 0) {
                problem = problemService.getById(problemId);
            }
            ProblemVO problemVO = ProblemVO.objToVo(problem);
            commentVO.setProblemVO(problemVO);

            commentVO.setIsLiked(commentIdHasThumbMap.getOrDefault(comment.getId(), false));
            return commentVO;
        }).collect(Collectors.toList());
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    public boolean doLikeComment(Long commentId, Long userId, HttpServletRequest request) {
        QueryWrapper<CommentThumb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commentId", commentId);
        queryWrapper.eq("userId", userId);
        Long count = commentThumbMapper.selectCount(queryWrapper);
        Comment comment = this.getById(commentId);
        if (Objects.equals(0L, count)) {
            comment.setThumbNum(comment.getThumbNum() + 1);
            this.updateById(comment);
            CommentThumb commentThumb = new CommentThumb();
            commentThumb.setCommentId(commentId);
            commentThumb.setUserId(userId);
            return commentThumbService.save(commentThumb);
        } else {
            comment.setThumbNum(comment.getThumbNum() - 1);
            this.updateById(comment);
            return commentThumbService.remove(queryWrapper);
        }
    }

    @Override
    public Page<Comment> searchFromEs(CommentQueryRequest commentQueryRequest) {
        Long id = commentQueryRequest.getId();
        String searchText = commentQueryRequest.getSearchText();
        Long userId = commentQueryRequest.getUserId();
        // es 起始页为 0
        long current = commentQueryRequest.getCurrent() - 1;
        long pageSize = commentQueryRequest.getPageSize();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        Long problemId = commentQueryRequest.getProblemId();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        if (problemId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("problemId", problemId));
        }
        FuzzyQueryBuilder fuzzyQuery = null;
        WildcardQueryBuilder wildcardQuery = null;
        // 按关键词检索
        if (StringUtils.isNotBlank(searchText)) {
            // 纠错匹配
            fuzzyQuery = QueryBuilders.fuzzyQuery("content", searchText)
                    .fuzziness(Fuzziness.AUTO);
            // 模糊匹配
            wildcardQuery = QueryBuilders.wildcardQuery("content", "*" + searchText + "*");
            /*boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.minimumShouldMatch(1);*/
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder);

        if (fuzzyQuery != null) {
            searchQueryBuilder.withQuery(fuzzyQuery);
        }
        if (wildcardQuery != null) {
            searchQueryBuilder.withQuery(wildcardQuery);
        }

        searchQueryBuilder.withPageable(pageRequest)
                .withSorts(sortBuilder);

        NativeSearchQuery searchQuery = searchQueryBuilder.build();
        SearchHits<CommentEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, CommentEsDTO.class);
        Page<Comment> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Comment> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<CommentEsDTO>> searchHitList = searchHits.getSearchHits();
            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            List<Comment> postList = baseMapper.selectBatchIds(postIdList);
            if (postList != null) {
                Map<Long, List<Comment>> idCommentMap = postList.stream().collect(Collectors.groupingBy(Comment::getId));
                postIdList.forEach(postId -> {
                    if (idCommentMap.containsKey(postId)) {
                        resourceList.add(idCommentMap.get(postId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), CommentEsDTO.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        return page;
    }

}

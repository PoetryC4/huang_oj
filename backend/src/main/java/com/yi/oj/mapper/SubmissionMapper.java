package com.yi.oj.mapper;

import com.yi.oj.model.entity.Submission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yi.oj.model.vo.SimpleSubmission;
import com.yi.oj.model.vo.UserRecordVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【submission(提交)】的数据库操作Mapper
* @createDate 2023-09-26 21:03:14
* @Entity com.yi.oj.model.entity.Submission
*/
public interface SubmissionMapper extends BaseMapper<Submission> {

    UserRecordVO getUserRecordTried(long id);

    List<SimpleSubmission> getSimpleSubmissionRes(long size, long offset, String title, Integer judgeStatus, Long id);

    Integer getSimpleSubmissionCount(long size, long offset, String title, Integer judgeStatus, Long id);
}





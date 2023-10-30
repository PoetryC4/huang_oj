package com.yioj.submissionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yioj.model.model.entity.Submission;
import com.yioj.model.model.vo.SimpleSubmission;
import com.yioj.model.model.vo.UserRecordVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【submission(提交)】的数据库操作Mapper
* @createDate 2023-09-26 21:03:14
* @Entity com.yi.oj.model.entity.Submission
*/
@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {

    UserRecordVO getUserRecordTried(long id);

    List<SimpleSubmission> getSimpleSubmissionRes(long size, long offset, String title, Integer judgeStatus, Long id);

    Integer getSimpleSubmissionCount(long size, long offset, String title, Integer judgeStatus, Long id);
}





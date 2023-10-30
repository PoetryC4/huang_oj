package com.yioj.model.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户战绩（脱敏）
 *
 */
@Data
public class UserRecordVO implements Serializable {

    /**
     * id
     */
    private Long id;

    private Integer hardSolved;
    private Integer hardTried;
    private Integer hardTotal;
    private Integer mediumSolved;
    private Integer mediumTried;
    private Integer mediumTotal;
    private Integer easySolved;
    private Integer easyTried;
    private Integer easyTotal;

    private static final long serialVersionUID = 1L;
}
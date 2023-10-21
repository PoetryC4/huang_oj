package com.huang.oj.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户战绩（脱敏）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
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
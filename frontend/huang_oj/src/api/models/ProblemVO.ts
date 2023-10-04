/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';
import type { UserVO } from './UserVO';

export type ProblemVO = {
    acceptance?: number;
    accpetedCount?: number;
    content?: string;
    createTime?: string;
    difficulty?: number;
    disLikeNum?: number;
    id?: number;
    isSolved?: number;
    isVip?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    solution?: string;
    submittedCount?: number;
    tags?: Array<string>;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userVO?: UserVO;
};

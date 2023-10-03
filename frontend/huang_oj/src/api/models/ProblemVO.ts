/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeConfig } from './JudgeConfig';
import type { ProblemTag } from './ProblemTag';
import type { UserVO } from './UserVO';

export type ProblemVO = {
    acceptance?: number;
    accpetedCount?: number;
    content?: string;
    createTime?: string;
    disLikeNum?: number;
    id?: number;
    isSolved?: boolean;
    isVip?: number;
    judgeConfig?: JudgeConfig;
    solution?: string;
    submittedCount?: number;
    tags?: ProblemTag;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userVO?: UserVO;
};

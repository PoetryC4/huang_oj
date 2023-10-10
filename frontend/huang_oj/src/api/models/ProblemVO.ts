/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { FunctionConfig } from './FunctionConfig';
import type { JudgeCases } from './JudgeCases';
import type { JudgeConfig } from './JudgeConfig';
import type { UserVO } from './UserVO';

export type ProblemVO = {
    acceptance?: number;
    accpetedCount?: number;
    content?: string;
    createTime?: string;
    difficulty?: number;
    disLikeNum?: number;
    functionConfig?: FunctionConfig;
    id?: number;
    isSolved?: number;
    isVip?: number;
    judgeCases?: JudgeCases;
    judgeConfig?: JudgeConfig;
    solution?: string;
    submittedCount?: number;
    tags?: Array<string>;
    thumbNum?: number;
    title?: string;
    updateTime?: string;
    userVO?: UserVO;
};

/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeResult } from './JudgeResult';
import type { UserVO } from './UserVO';

export type SubmissionVO = {
    code?: string;
    id?: number;
    judgeResult?: JudgeResult;
    language?: string;
    problemId?: number;
    submitTime?: string;
    userId?: number;
    userVO?: UserVO;
};

/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeInfo } from './JudgeInfo';
import type { ProblemVO } from './ProblemVO';
import type { UserVO } from './UserVO';

export type SubmissionVO = {
    code?: string;
    id?: number;
    judgeInfo?: JudgeInfo;
    language?: string;
    problemId?: number;
    problemVO?: ProblemVO;
    submitTime?: string;
    userId?: number;
    userVO?: UserVO;
};

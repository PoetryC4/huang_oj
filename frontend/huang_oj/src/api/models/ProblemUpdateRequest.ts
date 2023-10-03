/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';
import type { ProblemTag } from './ProblemTag';

export type ProblemUpdateRequest = {
    content?: string;
    id?: number;
    isVip?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    solution?: string;
    tags?: ProblemTag;
    title?: string;
};

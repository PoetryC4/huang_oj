/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';

export type ProblemUpdateRequest = {
    content?: string;
    difficulty?: number;
    id?: number;
    isVip?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    solution?: string;
    tags?: Array<string>;
    title?: string;
};

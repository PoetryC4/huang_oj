/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';

export type ProblemAddRequest = {
    content?: string;
    difficulty?: number;
    isVip?: number;
    judgeCase?: Array<JudgeCase>;
    judgeConfig?: JudgeConfig;
    solution?: string;
    tags?: Array<string>;
    title?: string;
    userId?: number;
};

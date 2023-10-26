/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { FunctionConfig } from './FunctionConfig';
import type { JudgeCases } from './JudgeCases';
import type { JudgeConfig } from './JudgeConfig';

export type ProblemAddRequest = {
    content?: string;
    difficulty?: number;
    functionConfig?: FunctionConfig;
    isVip?: number;
    judgeCases?: JudgeCases;
    judgeConfig?: JudgeConfig;
    solution?: string;
    tags?: Array<string>;
    title?: string;
    userId?: number;
};

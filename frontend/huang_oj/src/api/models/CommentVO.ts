/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ProblemVO } from './ProblemVO';
import type { UserVO } from './UserVO';

export type CommentVO = {
    content?: string;
    createTime?: string;
    id?: number;
    isLiked?: boolean;
    problemId?: number;
    problemVO?: ProblemVO;
    thumbNum?: number;
    updateTime?: string;
    userId?: number;
    userVO?: UserVO;
};

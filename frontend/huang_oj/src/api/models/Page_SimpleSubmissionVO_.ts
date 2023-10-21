/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { OrderItem } from './OrderItem';
import type { SimpleSubmissionVO } from './SimpleSubmissionVO';

export type Page_SimpleSubmissionVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: Array<OrderItem>;
    pages?: number;
    records?: Array<SimpleSubmissionVO>;
    searchCount?: boolean;
    size?: number;
    total?: number;
};

/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_JudgeResult_ } from '../models/BaseResponse_JudgeResult_';
import type { BaseResponse_Page_SimpleSubmissionVO_ } from '../models/BaseResponse_Page_SimpleSubmissionVO_';
import type { BaseResponse_Page_SubmissionVO_ } from '../models/BaseResponse_Page_SubmissionVO_';
import type { BaseResponse_SubmissionVO_ } from '../models/BaseResponse_SubmissionVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { ProblemSubmitQuest } from '../models/ProblemSubmitQuest';
import type { ProblemTestExampleRequest } from '../models/ProblemTestExampleRequest';
import type { SimpleSubmissionQueryQuest } from '../models/SimpleSubmissionQueryQuest';
import type { SubmissionQueryQuest } from '../models/SubmissionQueryQuest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class SubmissionControllerService {

    /**
     * doSubmit
     * @param problemSubmitQuest problemSubmitQuest
     * @returns BaseResponse_SubmissionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doSubmitUsingPost(
problemSubmitQuest: ProblemSubmitQuest,
): CancelablePromise<BaseResponse_SubmissionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/submission/add',
            body: problemSubmitQuest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteSubmission
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteSubmissionUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/submission/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getSubmissionVOById
     * @param id id
     * @returns BaseResponse_SubmissionVO_ OK
     * @throws ApiError
     */
    public static getSubmissionVoByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_SubmissionVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/submission/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listSubmissionVOByPage
     * @param submissionQueryQuest submissionQueryQuest
     * @returns BaseResponse_Page_SubmissionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listSubmissionVoByPageUsingPost(
submissionQueryQuest: SubmissionQueryQuest,
): CancelablePromise<BaseResponse_Page_SubmissionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/submission/list/page/vo',
            body: submissionQueryQuest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listMySubmissionVOByPage
     * @param simpleSubmissionQueryQuest simpleSubmissionQueryQuest
     * @returns BaseResponse_Page_SimpleSubmissionVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMySubmissionVoByPageUsingPost(
simpleSubmissionQueryQuest: SimpleSubmissionQueryQuest,
): CancelablePromise<BaseResponse_Page_SimpleSubmissionVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/submission/my/list/page/vo',
            body: simpleSubmissionQueryQuest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * testSubmit
     * @param problemTestExampleRequest problemTestExampleRequest
     * @returns BaseResponse_JudgeResult_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static testSubmitUsingPost(
problemTestExampleRequest: ProblemTestExampleRequest,
): CancelablePromise<BaseResponse_JudgeResult_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/submission/test',
            body: problemTestExampleRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}

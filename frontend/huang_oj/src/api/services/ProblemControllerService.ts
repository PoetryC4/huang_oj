/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_ProblemVO_ } from '../models/BaseResponse_Page_ProblemVO_';
import type { BaseResponse_ProblemVO_ } from '../models/BaseResponse_ProblemVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { ProblemAddRequest } from '../models/ProblemAddRequest';
import type { ProblemDislikeUpdateRequest } from '../models/ProblemDislikeUpdateRequest';
import type { ProblemQueryRequest } from '../models/ProblemQueryRequest';
import type { ProblemThumbUpdateRequest } from '../models/ProblemThumbUpdateRequest';
import type { ProblemUpdateRequest } from '../models/ProblemUpdateRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class ProblemControllerService {

    /**
     * addProblem
     * @param problemAddRequest problemAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addProblemUsingPost(
problemAddRequest: ProblemAddRequest,
): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/add',
            body: problemAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * deleteProblem
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteProblemUsingPost(
deleteRequest: DeleteRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * doDislikeProblem
     * @param problemDislikeUpdateRequest problemDislikeUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doDislikeProblemUsingPost(
problemDislikeUpdateRequest: ProblemDislikeUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/dislike',
            body: problemDislikeUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * editProblem
     * @param problemUpdateRequest problemUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editProblemUsingPost(
problemUpdateRequest: ProblemUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/edit',
            body: problemUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * getProblemVOById
     * @param id id
     * @returns BaseResponse_ProblemVO_ OK
     * @throws ApiError
     */
    public static getProblemVoByIdUsingGet(
id?: number,
): CancelablePromise<BaseResponse_ProblemVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/problem/get/vo',
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
     * doLikeProblem
     * @param problemThumbUpdateRequest problemThumbUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static doLikeProblemUsingPost(
problemThumbUpdateRequest: ProblemThumbUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/like',
            body: problemThumbUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * listProblemVOByPage
     * @param problemQueryRequest problemQueryRequest
     * @returns BaseResponse_Page_ProblemVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listProblemVoByPageUsingPost(
problemQueryRequest: ProblemQueryRequest,
): CancelablePromise<BaseResponse_Page_ProblemVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/list/page/vo',
            body: problemQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

    /**
     * updateProblem
     * @param problemUpdateRequest problemUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateProblemUsingPost(
problemUpdateRequest: ProblemUpdateRequest,
): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/problem/update',
            body: problemUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }

}

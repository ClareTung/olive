package com.olive.fpc.gateway.response;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 14:32
 */
public class TokenForbiddenResponse extends BaseResponse{

    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}

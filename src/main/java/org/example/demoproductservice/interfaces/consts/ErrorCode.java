package org.example.demoproductservice.interfaces.consts;

public enum ErrorCode {

    /* 공통 */
    INVALID_INPUT("입력이 올바르지 않습니다.", 1000L),
    INVALID_INPUT_TYPE("입력 타입이 올바르지 않습니다.", 1001L),
    METHOD_NOT_ALLOWED("지원하지 않는 메서드입니다.", 1002L),
    INTERNAL_SERVER_ERROR("서버 오류입니다.", 1003L),

    /* 브랜드 */
    BRAND_NOT_FOUND("브랜드가 존재하지 않습니다. ", 2000L),
    AT_LEAST_ONE_BRAND_REQUIRED("등록된 브랜드가 없습니다. 브랜드는 최소 1개 이상이어야 합니다.", 2001L),
    AT_LEAST_ONE_BRAND_REGISTER_ALL_CATEGORIES("적어도 1개 이상의 브랜드는 모든 카테고리의 상품을 등록해야 합니다.", 2002L),

    /* 카테고리 */
    CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다.", 3000L),
    AT_LEAST_ONE_CATEGORY_REQUIRED("등록된 카테고리가 없습니다. 카테고리는 최소 1개 이상이어야 합니다.", 3001L),

    /* 상품 */
    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다.", 4000L),
    AT_LEAST_ONE_PRODUCT_REQUIRED("등록된 상품이 없습니다. 상품은 최소 1개 이상이어야 합니다.", 4001L),
    AT_LEAST_ONE_PRODUCT_REGISTERED_TO_EACH_CATEGORY("적어도 1개 이상의 상품이 각 카테고리에 등록되어야 있어야 합니다.", 4002L)


    ;

    public final String message;
    public final Long code;

    ErrorCode(final String message, final Long code) {
        this.message = message;
        this.code = code;
    }

}

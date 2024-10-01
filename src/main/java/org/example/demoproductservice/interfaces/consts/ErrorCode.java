package org.example.demoproductservice.interfaces.consts;

public enum ErrorCode {

    /* 공통 */
    INVALID_INPUT("입력이 올바르지 않습니다.", 1000L),
    INVALID_INPUT_TYPE("입력 타입이 올바르지 않습니다.", 1001L),
    METHOD_NOT_ALLOWED("지원하지 않는 메서드입니다.", 1002L),
    INTERNAL_SERVER_ERROR("서버 오류입니다.", 1003L),
    /* 브랜드 */
    BRAND_NOT_FOUND("브랜드가 존재하지 않습니다. ", 2000L),
    /* 카테고리 */
    CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다.", 3000L),
    /* 상품 */
    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다.", 4000L);

    public final String message;
    public final Long code;

    ErrorCode(final String message, final Long code) {
        this.message = message;
        this.code = code;
    }

}

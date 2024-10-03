package org.example.demoproductservice.integration;

import org.example.demoproductservice.interfaces.consts.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.demoproductservice.interfaces.consts.ErrorCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {
    final String existingProductId = "1";
    final String nonExistingProductId = "100";

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @DisplayName("카테고리별 최저가격 상품으로 구성된 코디 조회 성공")
    @Test
    void testGetMinimumTotalCostProductSetSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.totalCost").value(34100),
                        jsonPath("$.data.items").isArray(),
                        jsonPath("$.data.items[0].categoryType").value("TOP"),
                        jsonPath("$.data.items[0].brandName").value("C"),
                        jsonPath("$.data.items[0].price").value(10000),
                        jsonPath("$.data.items[1].categoryType").value("OUTER"),
                        jsonPath("$.data.items[1].brandName").value("E"),
                        jsonPath("$.data.items[1].price").value(5000),
                        jsonPath("$.data.items[2].categoryType").value("BOTTOM"),
                        jsonPath("$.data.items[2].brandName").value("D"),
                        jsonPath("$.data.items[2].price").value(3000),
                        jsonPath("$.data.items[3].categoryType").value("SNEAKERS"),
                        jsonPath("$.data.items[3].brandName").value("A"),
                        jsonPath("$.data.items[3].price").value(9000),
                        jsonPath("$.data.items[4].categoryType").value("BAG"),
                        jsonPath("$.data.items[4].brandName").value("A"),
                        jsonPath("$.data.items[4].price").value(2000),
                        jsonPath("$.data.items[5].categoryType").value("HAT"),
                        jsonPath("$.data.items[5].brandName").value("D"),
                        jsonPath("$.data.items[5].price").value(1500),
                        jsonPath("$.data.items[6].categoryType").value("SOCKS"),
                        jsonPath("$.data.items[6].brandName").value("I"),
                        jsonPath("$.data.items[6].price").value(1700),
                        jsonPath("$.data.items[7].categoryType").value("ACCESSORY"),
                        jsonPath("$.data.items[7].brandName").value("F"),
                        jsonPath("$.data.items[7].price").value(1900)
                );
    }

    @Order(2)
    @Sql(statements = {"TRUNCATE TABLE products"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-products.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("카테고리별 최저가격 상품으로 구성된 코디 조회 실패 - 모든 카테고리에 상품이 등록되어 있지 않은 경우")
    @Test
    void testGetMinimumTotalCostProductSetFail() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost"))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_PRODUCT_REGISTERED_TO_EACH_CATEGORY.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_PRODUCT_REGISTERED_TO_EACH_CATEGORY.code)
                );
    }

    @Order(3)
    @Sql(statements = {"TRUNCATE TABLE categories"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-categories.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("카테고리별 최저가격 상품으로 구성된 코디 조회 실패 - 카테고리가 존재하지 않는 경우")
    @Test
    void testGetMinimumTotalCostProductSetFail2() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost"))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_CATEGORY_REQUIRED.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_CATEGORY_REQUIRED.code)
                );
    }

    @Order(4)
    @DisplayName("단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 브랜드의 코디 조회 성공")
    @Test
    void testGetMinimumTotalCostOneBrandProductSetSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost/one-brand"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.brandName").value("D"),
                        jsonPath("$.data.totalCost").value(36100),
                        jsonPath("$.data.items").isArray(),
                        jsonPath("$.data.items[0].categoryType").value("TOP"),
                        jsonPath("$.data.items[0].price").value(10100),
                        jsonPath("$.data.items[1].categoryType").value("OUTER"),
                        jsonPath("$.data.items[1].price").value(5100),
                        jsonPath("$.data.items[2].categoryType").value("BOTTOM"),
                        jsonPath("$.data.items[2].price").value(3000),
                        jsonPath("$.data.items[3].categoryType").value("SNEAKERS"),
                        jsonPath("$.data.items[3].price").value(9500),
                        jsonPath("$.data.items[4].categoryType").value("BAG"),
                        jsonPath("$.data.items[4].price").value(2500),
                        jsonPath("$.data.items[5].categoryType").value("HAT"),
                        jsonPath("$.data.items[5].price").value(1500),
                        jsonPath("$.data.items[6].categoryType").value("SOCKS"),
                        jsonPath("$.data.items[6].price").value(2400),
                        jsonPath("$.data.items[7].categoryType").value("ACCESSORY"),
                        jsonPath("$.data.items[7].price").value(2000)
                );
    }

    @Order(5)
    @Sql(statements = {"TRUNCATE TABLE products"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-products.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 브랜드의 코디 조회 실패 - 모든 카테고리의 상품을 등록한 브랜드가 없는 경우")
    @Test
    void testGetMinimumTotalCostOneBrandProductSetFail() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost/one-brand"))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_BRAND_REGISTER_ALL_CATEGORIES.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_BRAND_REGISTER_ALL_CATEGORIES.code)
                );
    }

    @Order(6)
    @Sql(statements = {"TRUNCATE TABLE categories"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-categories.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 브랜드의 코디 조회 실패 - 카테고리가 존재하지 않는 경우")
    @Test
    void testGetMinimumTotalCostOneBrandProductSetFail2() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost/one-brand"))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_CATEGORY_REQUIRED.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_CATEGORY_REQUIRED.code)
                );
    }

    @Order(7)
    @Sql(statements = {"TRUNCATE TABLE brands"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-brands.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("단일 브랜드로 전체 카테고리 상품을 구매할 경우, 상품 가격 총액이 가장 낮은 브랜드의 코디 조회 실패 - 브랜드가 존재하지 않는 경우")
    @Test
    void testGetMinimumTotalCostOneBrandProductSetFail3() throws Exception {
        mockMvc.perform(get("/api/v1/products/coordi-set/minimum-total-cost/one-brand"))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_BRAND_REQUIRED.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_BRAND_REQUIRED.code)
                );
    }

    @Order(8)
    @DisplayName("카테고리별 최저, 최고 가격 상품 정보 조회 성공")
    @Test
    void testGetLowestAndHighestPriceProductsSuccess() throws Exception {
        String categoryType = "TOP";

        mockMvc.perform(get("/api/v1/products/lowest-and-highest-price?categoryType={categoryType}", categoryType))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.categoryType").value("TOP"),
                        jsonPath("$.data.lowestPriceProduct.brandName").value("C"),
                        jsonPath("$.data.lowestPriceProduct.price").value(10000),
                        jsonPath("$.data.highestPriceProduct.brandName").value("I"),
                        jsonPath("$.data.highestPriceProduct.price").value(11400)
                );
    }

    @Order(9)
    @DisplayName("카테고리별 최저, 최고 가격 상품 정보 조회 실패 - 카테고리가 존재하지 않는 경우")
    @Test
    void testGetLowestAndHighestPriceProductsFail() throws Exception {
        String categoryType = "INVALID_CATEGORY_TOP";
        mockMvc.perform(get("/api/v1/products/lowest-and-highest-price?categoryType={categoryType}", categoryType))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(CATEGORY_NOT_FOUND.message),
                        jsonPath("$.error.code").value(CATEGORY_NOT_FOUND.code)
                );
    }

    @Order(10)
    @Sql(statements = {"TRUNCATE TABLE products"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:data-products.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("카테고리별 최저, 최고 가격 상품 정보 조회 실패 - 해당 카테고리에 등록된 상품이 없는 경우")
    @Test
    void testGetLowestAndHighestPriceProductsFail2() throws Exception {
        String categoryType = "TOP";
        mockMvc.perform(get("/api/v1/products/lowest-and-highest-price?categoryType={categoryType}", categoryType))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.error.message").value(AT_LEAST_ONE_PRODUCT_REGISTERED_TO_CATEGORY.message),
                        jsonPath("$.error.code").value(AT_LEAST_ONE_PRODUCT_REGISTERED_TO_CATEGORY.code)
                );
    }

    @Order(11)
    @DisplayName("상품 조회 실패")
    @Test
    void testFindProductFail() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}", nonExistingProductId))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.error.message").value(PRODUCT_NOT_FOUND.message),
                        jsonPath("$.error.code").value(PRODUCT_NOT_FOUND.code)
                );
    }

    @Order(12)
    @DisplayName("상품 조회 성공")
    @Test
    void testFindProductSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}", existingProductId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id").value(existingProductId),
                        jsonPath("$.data.categoryId").value(1),
                        jsonPath("$.data.brandId").value(1),
                        jsonPath("$.data.price").value(11200)
                );
    }

    @Order(13)
    @DisplayName("상품 등록 실패")
    @Test
    void testRegisterProductFail() throws Exception {
        Long categoryId = 0L;
        Long brandId = 3L;
        Long price = 55000L;
        String requestBody = "{\"categoryId\": \"" + categoryId + "\", \"brandId\": \"" + brandId + "\", \"price\": " + price + "}";


        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.error.message").value(ErrorCode.INVALID_INPUT.message + " categoryId는 1 이상 1,000,000,000 이하의 정수입니다."),
                        jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT.code)
                );
    }

    @Order(14)
    @DisplayName("상품 등록 성공")
    @Test
    void testRegisterProductSuccess() throws Exception {
        Long categoryId = 2L;
        Long brandId = 3L;
        Long price = 55000L;
        String requestBody = "{\"categoryId\": \"" + categoryId + "\", \"brandId\": \"" + brandId + "\", \"price\": " + price + "}";

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll((status().isOk()),
                        jsonPath("$.data.id").value(73),
                        jsonPath("$.data.categoryId").value(2),
                        jsonPath("$.data.brandId").value(3),
                        jsonPath("$.data.price").value(55000));
    }

    @Order(15)
    @DisplayName("상품 수정 실패")
    @Test
    void testUpdateProductFail() throws Exception {
        Long categoryId = 1L;
        Long brandId = 1L;
        Long price = 100_000_001L;
        String requestBody = "{\"id\": " + existingProductId + ", \"categoryId\": \"" + categoryId + "\", \"brandId\": \"" + brandId + "\", \"price\": " + price + "}";


        mockMvc.perform(patch("/api/v1/products/{productId}", existingProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpectAll((status().isBadRequest()),
                        jsonPath("$.error.message").value(ErrorCode.INVALID_INPUT.message + " price(상품 가격)는 100,000,000원 이하여야 합니다."),
                        jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT.code));
    }

    @Order(16)
    @DisplayName("상품 수정 성공")
    @Test
    void testUpdateProductSuccess() throws Exception {
        Long categoryId = 1L;
        Long brandId = 2L;
        Long price = 55000L;
        String requestBody = "{\"categoryId\": \"" + categoryId + "\", \"brandId\": \"" + brandId + "\", \"price\": " + price + "}";

        mockMvc.perform(
                        patch("/api/v1/products/{productId}", existingProductId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpectAll((status().isOk()),
                        jsonPath("$.data.id").value(existingProductId),
                        jsonPath("$.data.categoryId").value(1),
                        jsonPath("$.data.brandId").value(2),
                        jsonPath("$.data.price").value(55000));
    }

    @Order(17)
    @DisplayName("상품 삭제 실패")
    @Test
    void testDeleteProductFail() throws Exception {

        mockMvc.perform(delete("/api/v1/products/{productId}", nonExistingProductId))
                .andExpectAll((status().isNotFound()), jsonPath("$.error.message").value(PRODUCT_NOT_FOUND.message), jsonPath("$.error.code").value(PRODUCT_NOT_FOUND.code));
    }

    @Order(18)
    @DisplayName("상품 삭제 성공")
    @Test
    void testDeleteProductSuccess() throws Exception {

        mockMvc.perform(delete("/api/v1/products/{productId}", existingProductId)).andExpect(status().isOk());
    }

}

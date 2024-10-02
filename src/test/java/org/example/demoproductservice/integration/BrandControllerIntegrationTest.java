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

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrandControllerIntegrationTest {

    final String existingBrandId = "1";
    final String nonExistingBrandId = "100";

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @DisplayName("브랜드 조회 실패")
    @Test
    void testFindBrandFail() throws Exception {
        mockMvc.perform(get("/api/v1/brands/{brandId}", nonExistingBrandId)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error.message").value(ErrorCode.BRAND_NOT_FOUND.message)).andExpect(jsonPath("$.error.code").value(ErrorCode.BRAND_NOT_FOUND.code));
    }

    @Order(2)
    @DisplayName("브랜드 조회 성공")
    @Test
    void testFindBrandSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/brands/{brandId}", existingBrandId)).andExpect(status().isOk()).andExpect(jsonPath("$.data.id").value(existingBrandId)).andExpect(jsonPath("$.data.brandName").value("A"));
    }

    @Order(3)
    @DisplayName("브랜드 등록 실패")
    @Test
    void testRegisterBrandFail() throws Exception {
        String brandName = "";
        String requestBody = "{\"brandName\": \"" + brandName + "\"}";
        mockMvc.perform(
                post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.error.message", startsWith(ErrorCode.INVALID_INPUT.message)),
                jsonPath("$.error.code").value(ErrorCode.INVALID_INPUT.code));
    }

    @Order(4)
    @DisplayName("브랜드 등록 성공")
    @Test
    void testRegisterBrandSuccess() throws Exception {
        String brandName = "New brand";
        String requestBody = "{\"brandName\": \"" + brandName + "\"}";

        mockMvc.perform(
                post("/api/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id").value(10),
                        jsonPath("$.data.brandName").value(brandName)
                );
    }

    @Order(5)
    @DisplayName("브랜드 수정 실패")
    @Test
    void testUpdateBrandFail() throws Exception {
        String brandName = "Updated brand";
        String requestBody = "{\"brandName\": \"" + brandName + "\"}";

        mockMvc.perform(
                patch("/api/v1/brands/{brandId}", nonExistingBrandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.error.message").value(ErrorCode.BRAND_NOT_FOUND.message),
                jsonPath("$.error.code").value(ErrorCode.BRAND_NOT_FOUND.code)
        );
    }

    @Order(6)
    @DisplayName("브랜드 수정 성공")
    @Test
    void testUpdateBrandSuccess() throws Exception {
        String brandName = "Updated brand";
        String requestBody = "{\"brandName\": \"" + brandName + "\"}";

        mockMvc.perform(
                patch("/api/v1/brands/{brandId}", existingBrandId
                ).contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.data.id").value(existingBrandId),
                jsonPath("$.data.brandName").value("Updated brand")
        );
    }

    @Order(7)
    @DisplayName("브랜드 삭제 실패")
    @Test
    void testDeleteBrandFail() throws Exception {
        mockMvc.perform(
                delete("/api/v1/brands/{brandId}", nonExistingBrandId)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.error.message").value(ErrorCode.BRAND_NOT_FOUND.message),
                jsonPath("$.error.code").value(ErrorCode.BRAND_NOT_FOUND.code)
        );
    }

    @Order(8)
    @DisplayName("브랜드 삭제 성공")
    @Test
    void testDeleteBrandSuccess() throws Exception {
        mockMvc.perform(
                delete("/api/v1/brands/{brandId}", existingBrandId)
        ).andExpect(
                status().isOk()
        );
    }
}
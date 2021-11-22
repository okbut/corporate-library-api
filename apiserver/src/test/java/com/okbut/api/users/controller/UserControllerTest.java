package com.okbut.api.users.controller;

import com.okbut.api.ApiDocumentUtils;
import com.okbut.api.users.application.UserService;
import com.okbut.api.users.domain.Member;
import com.okbut.error.expcetion.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DisplayName("UserController 클래스")
@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("/users/{id}")
    class Describe_findUser {

        @Nested
        @DisplayName("만약 유효한 사용자 식별자가 주어진다면")
        class Context_with_valid_user_id {
            final Long validUserId = 1L;
            final Member desiredMember = Member.builder()
                    .id(validUserId)
                    .build();

            @BeforeEach
            void mocking() {
                given(userService.findUser(validUserId))
                        .willReturn(desiredMember);
            }

            @Test
            @DisplayName("주어진 식별자를 가진 사용자 정보를 리턴한다")
            void It_finds_a_user_by_given_userId() throws Exception {
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get("/users/{id}", validUserId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(validUserId.intValue())))
                        .andDo(MockMvcRestDocumentation.document("users-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("id").description("식별자")),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("식별자"))));

                verify(userService).findUser(anyLong());
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;
            final Member desiredMember = Member.builder()
                    .id(invalidUserId)
                    .build();

            @BeforeEach
            void mocking() {
                given(userService.findUser(invalidUserId))
                        .willThrow(new UserNotFoundException(invalidUserId));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() throws Exception {
                mockMvc.perform(
                                RestDocumentationRequestBuilders.get("/users/{id}", invalidUserId)
                                        .accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", containsString(invalidUserId.toString())));

                verify(userService).findUser(anyLong());
            }
        }
    }
}

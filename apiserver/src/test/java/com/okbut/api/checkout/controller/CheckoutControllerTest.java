package com.okbut.api.checkout.controller;

import com.okbut.api.ApiDocumentUtils;
import com.okbut.api.books.domain.Book;
import com.okbut.api.checkout.application.CheckoutService;
import com.okbut.api.checkout.domain.CheckOut;
import com.okbut.api.checkout.domain.CheckOutFixtures;
import com.okbut.api.users.domain.User;
import com.okbut.error.expcetion.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DisplayName("CheckoutController 클래스")
@WebMvcTest(CheckoutController.class)
@AutoConfigureRestDocs
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Nested
    @DisplayName("GET /users/{userId}/checkout/history 요청은")
    class Describe_get_users_user_id_checkout_history {
        final List<CheckOut> expectedCheckoutHistoryList = new ArrayList<>();
        final User validUser = User.builder().id(1L).build();
        final Book validBook = Book.builder().id(1L).build();
        final CheckOut returnedCheckout = CheckOutFixtures.returnedCheckout(validUser, validBook);

        @Nested
        @DisplayName("만약 대출 이력이 있는 유효한 사용자 식별자가 주어진다면")
        class Context_with_valid_user_id_and_have_checkout_history {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void setUp() {
                expectedCheckoutHistoryList.add(returnedCheckout);

                given(checkoutService.listCheckoutHistory(any(Long.class)))
                        .willReturn(expectedCheckoutHistoryList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 대출 이력을 리턴한다")
            void It_returns_checkout_history_list_by_given_user_id() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout/history", validUserId);

                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[*]", hasSize(expectedCheckoutHistoryList.size())))
                        .andExpect(jsonPath("$[0].returnDtm", notNullValue()))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-history-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자")),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("식별자"),
                                        fieldWithPath("[].checkoutDtm").type(JsonFieldType.STRING).description("대출일시"),
                                        fieldWithPath("[].dueDtm").type(JsonFieldType.STRING).description("반납예정일"),
                                        fieldWithPath("[].returnDtm").type(JsonFieldType.STRING).optional().description("반납일시"),
                                        fieldWithPath("[].isRenewed").type(JsonFieldType.NUMBER).description("대출연장여부"),
                                        subsectionWithPath("[].user").type(JsonFieldType.OBJECT).description("사용자식별자"),
                                        subsectionWithPath("[].book").type(JsonFieldType.OBJECT).description("도서식별자")
                                )));

                verify(checkoutService).listCheckoutHistory(anyLong());
            }
        }

        @Nested
        @DisplayName("만약 대출 이력이 없는 유효한 사용자 식별자가 주어진다면")
        class Context_with_valid_user_id_and_have_not_checkout_history {

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckoutHistory(any(Long.class)))
                        .willReturn(expectedCheckoutHistoryList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 빈 대출 이력 목록을 리턴한다")
            void It_returns_empty_list() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout/history", validUser.getId());
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[*]", hasSize(expectedCheckoutHistoryList.size())))
                        .andExpect(jsonPath("$[*]", empty()))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-history-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자"))));

                verify(checkoutService).listCheckoutHistory(anyLong());
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckoutHistory(invalidUserId))
                        .willThrow(new UserNotFoundException(invalidUserId));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout/history", invalidUserId);
                mockMvc.perform(request)
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", containsString(invalidUserId.toString())))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-history-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자"))));

                verify(checkoutService).listCheckoutHistory(anyLong());
            }
        }
    }

    @Nested
    @DisplayName("GET /users/{userId}/checkout 요청은")
    class Describe_get_users_user_id_checkout {
        final List<CheckOut> expectedCheckoutList = new ArrayList<>();
        final User validUser = User.builder().id(1L).build();
        final Book validBook = Book.builder().id(1L).build();
        final CheckOut duringCheckout = CheckOutFixtures.duringCheckout(validUser, validBook);

        @Nested
        @DisplayName("만약 대출 중인 사용자의 유효한 식별자가 주어진다면")
        class Context_with_valid_user_id_and_during_checkout {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void setUp() {
                expectedCheckoutList.add(duringCheckout);

                given(checkoutService.listCheckout(any(Long.class)))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 대출 현황 목록을 리턴한다")
            void It_returns_checkout_list_by_given_user_id() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout", validUserId);

                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[*]", hasSize(expectedCheckoutList.size())))
                        .andExpect(jsonPath("$[0].returnDtm", nullValue()))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자")),
                                responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("식별자"),
                                        fieldWithPath("[].checkoutDtm").type(JsonFieldType.STRING).description("대출일시"),
                                        fieldWithPath("[].dueDtm").type(JsonFieldType.STRING).description("반납예정일"),
                                        fieldWithPath("[].returnDtm").type(JsonFieldType.STRING).optional().description("반납일시"),
                                        fieldWithPath("[].isRenewed").type(JsonFieldType.NUMBER).description("대출연장여부"),
                                        subsectionWithPath("[].user").type(JsonFieldType.OBJECT).description("사용자식별자"),
                                        subsectionWithPath("[].book").type(JsonFieldType.OBJECT).description("도서식별자")
                                )));

                verify(checkoutService).listCheckout(anyLong());
            }
        }

        @Nested
        @DisplayName("만약 대출 중이 아닌 사용자의 유효한 식별자가 주어진다면")
        class Context_with_valid_user_id_and_not_during_checkout {

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckout(any(Long.class)))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 빈 대출 현황 목록을 리턴한다")
            void It_returns_empty_list() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout", validUser.getId());
                mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[*]", hasSize(expectedCheckoutList.size())))
                        .andExpect(jsonPath("$[*]", empty()))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자"))));

                verify(checkoutService).listCheckout(anyLong());
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckout(invalidUserId))
                        .willThrow(new UserNotFoundException(invalidUserId));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() throws Exception {
                var request = RestDocumentationRequestBuilders.get("/users/{userId}/checkout", invalidUserId);
                mockMvc.perform(request)
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", containsString(invalidUserId.toString())))
                        .andDo(MockMvcRestDocumentation.document("list-checkout-get",
                                ApiDocumentUtils.getDocumentRequest(),
                                ApiDocumentUtils.getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("userId").description("식별자"))));

                verify(checkoutService).listCheckout(anyLong());
            }
        }
    }
}

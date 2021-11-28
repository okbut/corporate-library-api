package com.okbut.api.checkout.application;

import com.okbut.api.books.domain.Book;
import com.okbut.api.checkout.domain.CheckOut;
import com.okbut.api.checkout.domain.CheckOutFixtures;
import com.okbut.api.checkout.domain.CheckoutRepository;
import com.okbut.api.users.application.UserService;
import com.okbut.api.users.domain.User;
import com.okbut.error.expcetion.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("CheckOutService 클래스")
class CheckoutServiceTest {

    private final CheckoutRepository checkoutRepository = mock(CheckoutRepository.class);
    private final UserService userService = mock(UserService.class);
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(checkoutRepository, userService);
    }

    @Nested
    @DisplayName("findCheckoutListOfUser 메소드는")
    class Describe_findCheckoutListOfUser {
        final List<CheckOut> expectedCheckoutList = new ArrayList<>();
        final User validUser = User.builder().id(1L).build();
        final Book validBook = Book.builder().id(1L).build();
        final CheckOut firstCheckout = CheckOutFixtures.firstCheckoutByUser(validUser, validBook);

        @Nested
        @DisplayName("만약 대여 이력이 있는 유효한 사용자 식별자로 대여 목록을 조회한다면")
        class Context_with_valid_user_id_and_check_out_list {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void mocking() {
                expectedCheckoutList.add(firstCheckout);

                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUser(validUser))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 대여 목록을 리턴한다")
            void It_returns_checkout_list_by_given_user_id() {
                final List<CheckOut> actualCheckoutList = checkoutService.findCheckoutListOfUser(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutList.size());
                assertThat(actualCheckoutList.get(0).getUser().getId()).isEqualTo(validUserId);

                verify(checkoutRepository).findByUser(validUser);
            }
        }

        @Nested
        @DisplayName("만약 대여 이력이 없는 유효한 사용자 식별자로 대여 목록을 조회한다면")
        class Context_with_valid_user_id_and_no_check_out_list {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void mocking() {
                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUser(validUser))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("빈 목록을 리턴한다")
            void It_returns_empty_list() {
                final List<CheckOut> actualCheckoutList = checkoutService.findCheckoutListOfUser(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutList.size());
                assertThat(actualCheckoutList).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;

            @BeforeEach
            void mocking() {
                given(checkoutService.findCheckoutListOfUser(invalidUserId))
                        .willThrow(new UserNotFoundException("사용자를 찾을 수 없습니다"));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() {
                assertThatThrownBy(() -> checkoutService.findCheckoutListOfUser(invalidUserId))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }
}

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckOutService 클래스")
class CheckoutServiceTest {

    @Mock
    private CheckoutRepository checkoutRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Nested
    @DisplayName("listCheckoutHistory 메소드는")
    class Describe_listCheckoutHistory {
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

                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUserAndReturnDtmNotNull(validUser))
                        .willReturn(expectedCheckoutHistoryList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 대출 이력을 리턴한다")
            void It_returns_checkout_history_list_by_given_user_id() {
                final List<CheckOut> actualCheckoutList = checkoutService.listCheckoutHistory(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutHistoryList.size());
                assertThat(actualCheckoutList.get(0).getUser().getId()).isEqualTo(validUserId);
                assertThat(actualCheckoutList.get(0).getReturnDtm()).isNotNull();

                verify(checkoutRepository).findByUserAndReturnDtmNotNull(validUser);
            }
        }

        @Nested
        @DisplayName("만약 대출 이력이 없는 유효한 사용자 식별자가 주어진다면")
        class Context_with_valid_user_id_and_have_not_checkout_history {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void setUp() {
                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUserAndReturnDtmNotNull(validUser))
                        .willReturn(expectedCheckoutHistoryList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 빈 대출 이력 목록을 리턴한다")
            void It_returns_empty_list() {
                final List<CheckOut> actualCheckoutList = checkoutService.listCheckoutHistory(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutHistoryList.size());
                assertThat(actualCheckoutList).isEmpty();
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckoutHistory(invalidUserId))
                        .willThrow(new UserNotFoundException("사용자를 찾을 수 없습니다"));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() {
                assertThatThrownBy(() -> checkoutService.listCheckoutHistory(invalidUserId))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("listCheckout 메소드는")
    class Describe_listCheckout {
        List<CheckOut> expectedCheckoutList = new ArrayList<>();
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

                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUserAndReturnDtmNull(validUser))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 대출 현황 목록을 리턴한다")
            void It_returns_checkout_list_by_given_user_id() {
                final List<CheckOut> actualCheckoutList = checkoutService.listCheckout(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutList.size());
                assertThat(actualCheckoutList.get(0).getUser().getId()).isEqualTo(validUserId);
                assertThat(actualCheckoutList.get(0).getReturnDtm()).isNull();

                verify(checkoutRepository).findByUserAndReturnDtmNull(any(User.class));
            }
        }

        @Nested
        @DisplayName("만약 대출 중이 아닌 사용자의 유효한 식별자가 주어진다면")
        class Context_with_valid_user_id_and_not_during_checkout {
            final Long validUserId = validUser.getId();

            @BeforeEach
            void setUp() {
                given(userService.findUser(validUserId))
                        .willReturn(validUser);

                given(checkoutRepository.findByUserAndReturnDtmNull(validUser))
                        .willReturn(expectedCheckoutList);
            }

            @Test
            @DisplayName("해당 사용자 식별자의 빈 대출 현황 목록을 리턴한다")
            void It_returns_empty_list() {
                final List<CheckOut> actualCheckoutList = checkoutService.listCheckout(validUserId);
                assertThat(actualCheckoutList).hasSize(expectedCheckoutList.size());
                assertThat(actualCheckoutList).isEmpty();

                verify(checkoutRepository).findByUserAndReturnDtmNull(any(User.class));
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 사용자 식별자가 주어진다면")
        class Context_with_invalid_user_id {
            final Long invalidUserId = 1L - 100L;

            @BeforeEach
            void setUp() {
                given(checkoutService.listCheckout(invalidUserId))
                        .willThrow(new UserNotFoundException("사용자를 찾을 수 없습니다"));
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 에러 메시지를 응답한다")
            void It_responds_user_not_found_error_message() {
                assertThatThrownBy(() -> checkoutService.listCheckout(invalidUserId))
                        .isInstanceOf(UserNotFoundException.class);

                verifyNoMoreInteractions(checkoutRepository);
            }
        }
    }
}

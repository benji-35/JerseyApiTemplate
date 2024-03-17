package domain;

import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private static final String EMAIL_CORRECT_FORMAT = "email@email.com";
    private static final String PASSWORD_CORRECT = "01234567";
    private static final String PSEUDO_CORRECT = "My pseudo";
    private static final String NAME_CORRECT = "My Name";

    @Nested
    public class CreateNewAccount {
        private static final String PSEUDO_NOT_CORRECT = null;
        private static final String NAME_NOT_CORRECT = null;
        private static final String EMAIL_BAD_FORMAT = "";
        private static final String PASSWORD_TOO_SHORT = "0123456";

        @Test
        void whenCreateNewAccount_withBadEmailFormat_thenThrowValidationException() {
            assertThrows(AccountValidationException.class, () -> {
                new Account(
                        EMAIL_BAD_FORMAT,
                        PASSWORD_CORRECT.toCharArray(),
                        PSEUDO_CORRECT,
                        NAME_CORRECT
                );
            });
        }

        @Test
        void whenCreateNewAccount_withPasswordTooShort_thenThrowValidationException() {
            assertThrows(AccountValidationException.class, () -> {
                new Account(
                        EMAIL_CORRECT_FORMAT,
                        PASSWORD_TOO_SHORT.toCharArray(),
                        PSEUDO_CORRECT,
                        NAME_CORRECT
                );
            });
        }

        @Test
        void whenCreateNewAccount_withPseudoNull_thenThrowValidationException() {
            assertThrows(AccountValidationException.class, () -> {
                new Account(
                        EMAIL_CORRECT_FORMAT,
                        PASSWORD_CORRECT.toCharArray(),
                        PSEUDO_NOT_CORRECT,
                        NAME_CORRECT
                );
            });
        }

        @Test
        void whenCreateNewAccount_withNameNull_thenThrowValidationException() {
            assertThrows(AccountValidationException.class, () -> {
                new Account(
                        EMAIL_CORRECT_FORMAT,
                        PASSWORD_CORRECT.toCharArray(),
                        PSEUDO_CORRECT,
                        NAME_NOT_CORRECT
                );
            });
        }

        @Test
        void whenCreateNewAccount_withoutProblem_thenNoThrows() {
            assertDoesNotThrow(() -> {
                new Account(
                        EMAIL_CORRECT_FORMAT,
                        PASSWORD_CORRECT.toCharArray(),
                        PSEUDO_CORRECT,
                        NAME_CORRECT
                );
            });
        }

    }

    @Nested
    public class PasswordChecker {
        private static final String BAD_PASSWORD = "01234568";
        private Account account;

        @BeforeEach
        void initAccount() throws AccountValidationException {
            account = new Account(
                    EMAIL_CORRECT_FORMAT,
                    PASSWORD_CORRECT.toCharArray(),
                    PSEUDO_CORRECT,
                    NAME_CORRECT
            );
        }

        @Test
        void checkPassword_whenPasswordCorrect_thenReturnTrue() {
            assertTrue(account.isValidPassword(PASSWORD_CORRECT.toCharArray()));
        }

        @Test
        void checkPassword_whenPasswordIncorrect_thenReturnFalse() {
            assertFalse(account.isValidPassword(BAD_PASSWORD.toCharArray()));
        }
    }

    @Nested
    public class Getter {
        private Account account;
        private static final UUID ACCOUNT_ID = UUID.randomUUID();

        @BeforeEach
        void initAccount() throws AccountValidationException {
            account = new Account(
                    ACCOUNT_ID,
                    EMAIL_CORRECT_FORMAT,
                    PASSWORD_CORRECT.toCharArray(),
                    PSEUDO_CORRECT,
                    NAME_CORRECT
            );
        }

        @Test
        void getEmail() {
            assertEquals(EMAIL_CORRECT_FORMAT, account.getEmail());
        }
        @Test
        void getUsername() {
            assertEquals(PSEUDO_CORRECT, account.getUsername());
        }
        @Test
        void getName() {
            assertEquals(NAME_CORRECT, account.getName());
        }

        @Test
        void getId() {
            assertEquals(ACCOUNT_ID, account.getId());
        }
    }

}

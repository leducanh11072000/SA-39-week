package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private static final Money ZERO = new Money(BigDecimal.ZERO, Currency.getInstance("USD"));

    @Test
    void GivenZeroBalance_WhenCreated_ThenAcceptsEmptyWallet() {
        var account = new Account("A", ZERO);
        assertAll(() -> assertEquals("A", account.id()), () -> assertEquals(ZERO, account.balance()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void GivenBlankId_WhenCreated_ThenRejects(String id) {
        assertThrows(IllegalArgumentException.class, () -> new Account(id, ZERO));
    }

    @Test
    void GivenNullComponent_WhenCreated_ThenRejects() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> new Account(null, ZERO)),
                () -> assertThrows(NullPointerException.class, () -> new Account("A", null)));
    }
}

package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class TransferResultTest {
    @Test
    void GivenSuccess_WhenRendered_ThenUsesRecordComponents() {
        var money = new Money(new BigDecimal("10"), Currency.getInstance("USD"));
        TransferResult result = new TransferResult.Success(new Account("A", money), new Account("B", money), money);
        assertAll(() -> assertEquals("Transferred 10.00 USD from A to B", result.message()),
                () -> assertTrue(result instanceof TransferResult.Success));
    }

    @ParameterizedTest
    @CsvSource({"SAME_ACCOUNT,source and destination must differ", "CURRENCY_MISMATCH,currencies must match",
            "NON_POSITIVE_AMOUNT,amount must be positive", "INSUFFICIENT_FUNDS,insufficient funds"})
    void GivenRejected_WhenRendered_ThenExplainsBusinessReason(TransferResult.RejectionReason reason, String text) {
        assertEquals("Transfer rejected: " + text, new TransferResult.Rejected(reason).message());
    }

    @Test
    void GivenFailed_WhenRendered_ThenExplainsTechnicalFailure() {
        assertEquals("Transfer failed: STORAGE_UNAVAILABLE", new TransferResult.Failed("STORAGE_UNAVAILABLE").message());
    }

    @Test
    void GivenInvalidResultComponents_WhenConstructed_ThenRejects() {
        var money = new Money(BigDecimal.ONE, Currency.getInstance("USD"));
        var account = new Account("A", money);
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new TransferResult.Success(null, account, money)),
                () -> assertThrows(NullPointerException.class, () -> new TransferResult.Success(account, null, money)),
                () -> assertThrows(NullPointerException.class, () -> new TransferResult.Success(account, account, null)),
                () -> assertThrows(NullPointerException.class, () -> new TransferResult.Rejected(null)),
                () -> assertThrows(NullPointerException.class, () -> new TransferResult.Failed(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> new TransferResult.Failed(" ")));
    }
}

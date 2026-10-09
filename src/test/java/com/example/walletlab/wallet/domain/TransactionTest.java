package com.example.walletlab.wallet.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.walletlab.wallet.domain.TransactionAnalyticsTest.*;
import static com.example.walletlab.wallet.domain.Transaction.Status.*;

class TransactionTest {
    @Test void GivenInvalidHistoryFields_WhenConstructed_ThenRejects() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> success(" ", "1")),
                () -> assertThrows(NullPointerException.class, () -> success(null, "1")),
                () -> assertThrows(NullPointerException.class, () -> tx("1", "A", "1", USD, null)),
                () -> assertThrows(IllegalArgumentException.class, () -> tx("1", " ", "1", USD, SUCCESS)),
                () -> assertThrows(NullPointerException.class, () -> new Transaction("1", "A", "B", null, SUCCESS)));
    }
    @Test void GivenInvalidSuccess_WhenConstructed_ThenRejects() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> success("1", "0")),
                () -> assertThrows(IllegalArgumentException.class, () -> tx("1", "recipient", "1", USD, SUCCESS)));
    }
    @Test void GivenRejectedZero_WhenConstructed_ThenPreservesAttempt() {
        assertEquals(REJECTED, tx("1", "A", "0", USD, REJECTED).status());
    }
}

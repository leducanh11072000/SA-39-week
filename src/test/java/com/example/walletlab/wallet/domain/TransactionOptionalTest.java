package com.example.walletlab.wallet.domain;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.walletlab.wallet.domain.TransactionAnalyticsTest.*;

class TransactionOptionalTest {
    @Test void GivenKnownId_WhenLookedUp_ThenMapsValue() {
        var item = success("1", "1");
        var analytics = new TransactionAnalytics(List.of(item));
        assertEquals(Optional.of(item.amount()), analytics.findById("1").map(Transaction::amount));
    }
    @Test void GivenUnknownId_WhenLookedUp_ThenEmptyOrExplicitException() {
        var analytics = new TransactionAnalytics(List.of());
        assertAll(() -> assertEquals(Optional.empty(), analytics.findById("missing")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> analytics.findById("missing").orElseThrow(() -> new IllegalArgumentException("Not found"))));
    }
    @Test void GivenPresentValue_WhenDefaultEvaluated_ThenOrElseIsEagerAndOrElseGetIsLazy() {
        var calls = new AtomicInteger();
        var value = new TransactionAnalytics(List.of(success("1", "1"))).findById("1").map(Transaction::id);
        assertEquals("1", value.orElse("fallback-" + calls.incrementAndGet()));
        assertEquals(1, calls.get());
        assertEquals("1", value.orElseGet(() -> "fallback-" + calls.incrementAndGet()));
        assertEquals(1, calls.get());
    }
    @Test void GivenAbsentValue_WhenFallbackRequested_ThenSupplierRunsOnce() {
        var calls = new AtomicInteger();
        var result = new TransactionAnalytics(List.of()).findById("missing")
                .flatMap(t -> Optional.ofNullable(t.id()))
                .orElseGet(() -> "fallback-" + calls.incrementAndGet());
        assertAll(() -> assertEquals("fallback-1", result), () -> assertEquals(1, calls.get()));
    }
}

package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.walletlab.wallet.domain.Transaction.Status.*;

class TransactionAnalyticsTest {
    static final Currency USD = Currency.getInstance("USD");
    static final Currency VND = Currency.getInstance("VND");
    static Money money(String amount, Currency currency) { return new Money(new BigDecimal(amount), currency); }
    static Transaction tx(String id, String source, String amount, Currency currency, Transaction.Status status) {
        return new Transaction(id, source, "recipient", money(amount, currency), status);
    }
    static Transaction success(String id, String amount) { return tx(id, "A", amount, USD, SUCCESS); }

    @Test void GivenMixedStatuses_WhenFiltered_ThenOnlySuccessRemains() {
        var good = success("1", "1");
        var analytics = new TransactionAnalytics(List.of(good, tx("2", "A", "9", USD, REJECTED), tx("3", "A", "8", USD, FAILED)));
        assertEquals(List.of(good), analytics.successfulTransactions());
    }
    @Test void GivenEmptyHistory_WhenAnalyzed_ThenReturnsEmptyAndZero() {
        var analytics = new TransactionAnalytics(List.of());
        assertAll(() -> assertTrue(analytics.totalsBySourceAccount().isEmpty()),
                () -> assertTrue(analytics.topFiveSuccessful(USD).isEmpty()),
                () -> assertTrue(analytics.successfulTransactions().isEmpty()),
                () -> assertEquals(money("0", USD), analytics.totalSuccessful(USD)));
    }
    @Test void GivenTwoSources_WhenGrouped_ThenTotalsAreIndependent() {
        var analytics = new TransactionAnalytics(List.of(success("1", "10"), success("2", "20"), tx("3", "B", "7", USD, SUCCESS)));
        var totals = analytics.totalsBySourceAccount();
        assertAll(() -> assertEquals(2, totals.size()),
                () -> assertEquals(money("30", USD), totals.get(new TransactionAnalytics.AccountCurrency("A", USD))),
                () -> assertEquals(money("7", USD), totals.get(new TransactionAnalytics.AccountCurrency("B", USD))));
    }
    @Test void GivenMixedCurrencies_WhenAggregated_ThenNeverCombinesCurrencies() {
        var analytics = new TransactionAnalytics(List.of(success("1", "10"), tx("2", "A", "1000", VND, SUCCESS)));
        assertAll(() -> assertEquals(2, analytics.totalsBySourceAccount().size()),
                () -> assertEquals(money("10", USD), analytics.totalSuccessful(USD)),
                () -> assertEquals(money("1000", VND), analytics.totalSuccessful(VND)));
    }
    @Test void GivenDecimals_WhenReduced_ThenTotalIsExact() {
        assertEquals(money("0.30", USD), new TransactionAnalytics(List.of(success("1", "0.10"), success("2", "0.20"))).totalSuccessful(USD));
    }
    @Test void GivenIdenticalDuplicate_WhenAnalyzed_ThenCountsOnce() {
        var first = success("1", "10");
        var analytics = new TransactionAnalytics(List.of(first, success("1", "10.00")));
        assertAll(() -> assertEquals(List.of(first), analytics.history()),
                () -> assertEquals(1, analytics.transactionIds().size()),
                () -> assertEquals(money("10", USD), analytics.totalSuccessful(USD)));
    }
    @Test void GivenConflictingId_WhenIndexed_ThenRejectsInsteadOfChoosingOne() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalytics(List.of(success("1", "10"), success("1", "11"))));
    }
    @Test void GivenMoreThanFive_WhenRanked_ThenReturnsFiveLargest() {
        var analytics = new TransactionAnalytics(List.of(success("1", "1"), success("2", "2"), success("3", "3"), success("4", "4"), success("5", "5"), success("6", "6")));
        assertEquals(List.of("6", "5", "4", "3", "2"), analytics.topFiveSuccessful(USD).stream().map(Transaction::id).toList());
    }
    @Test void GivenTiedAmounts_WhenRanked_ThenUsesIdAscending() {
        var analytics = new TransactionAnalytics(List.of(success("b", "10"), success("a", "10")));
        assertEquals(List.of("a", "b"), analytics.topFiveSuccessful(USD).stream().map(Transaction::id).toList());
    }
    @Test void GivenOtherCurrencyAndRejected_WhenRanked_ThenExcludesThem() {
        var good = success("1", "1");
        var analytics = new TransactionAnalytics(List.of(good, tx("2", "A", "999", USD, REJECTED), tx("3", "A", "999", VND, SUCCESS)));
        assertEquals(List.of(good), analytics.topFiveSuccessful(USD));
    }
    @Test void GivenMutableInput_WhenCallerClearsIt_ThenSnapshotSurvives() {
        var input = new ArrayList<>(List.of(success("1", "1")));
        var analytics = new TransactionAnalytics(input);
        input.clear();
        assertEquals(1, analytics.history().size());
    }
    @Test void GivenResults_WhenCallerMutates_ThenRejectsMutation() {
        var item = success("1", "1");
        var analytics = new TransactionAnalytics(List.of(item));
        assertAll(() -> assertThrows(UnsupportedOperationException.class, () -> analytics.history().add(item)),
                () -> assertThrows(UnsupportedOperationException.class, () -> analytics.successfulTransactions().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> analytics.topFiveSuccessful(USD).clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> analytics.totalsBySourceAccount().clear()),
                () -> assertThrows(UnsupportedOperationException.class, () -> analytics.transactionIds().clear()));
    }
    @Test void GivenInputOrder_WhenRanking_ThenHistoryOrderIsPreserved() {
        var input = List.of(success("1", "1"), success("2", "2"));
        var analytics = new TransactionAnalytics(input);
        analytics.topFiveSuccessful(USD);
        assertEquals(input, analytics.history());
    }
    @Test void GivenNullRow_WhenConstructed_ThenRejects() {
        var input = new ArrayList<Transaction>(); input.add(null);
        assertAll(() -> assertThrows(NullPointerException.class, () -> new TransactionAnalytics(input)),
                () -> assertThrows(NullPointerException.class, () -> new TransactionAnalytics(null)));
    }
    @Test void GivenOnlyFailures_WhenAggregated_ThenZeroAndNoGroups() {
        var analytics = new TransactionAnalytics(List.of(tx("1", "A", "8", USD, FAILED)));
        assertAll(() -> assertEquals(money("0", USD), analytics.totalSuccessful(USD)),
                () -> assertTrue(analytics.totalsBySourceAccount().isEmpty()));
    }
}

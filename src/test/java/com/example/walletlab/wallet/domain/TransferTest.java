package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class TransferTest {
    private static Money money(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

    @ParameterizedTest
    @CsvSource({"USD,100,25,30,70,55", "VND,1000,200,1000,0,1200", "USD,0.30,0,0.10,0.20,0.10"})
    void GivenValidTransfer_WhenExecuted_ThenConservesMoneyAndLeavesInputsUnchanged(
            String code, String start, String end, String amount, String expectedSource, String expectedDestination) {
        var source = new Account("A", money(start, code));
        var destination = new Account("B", money(end, code));
        var transfer = new Transfer(source, destination, money(amount, code));
        var result = assertInstanceOf(TransferResult.Success.class, transfer.execute());
        assertAll(
                () -> assertEquals(money(expectedSource, code), result.source().balance()),
                () -> assertEquals(money(expectedDestination, code), result.destination().balance()),
                () -> assertEquals(source.balance().add(destination.balance()),
                        result.source().balance().add(result.destination().balance())),
                () -> assertEquals(money(start, code), source.balance()),
                () -> assertEquals(money(end, code), destination.balance()),
                () -> assertEquals("A", result.source().id()),
                () -> assertEquals("B", result.destination().id()),
                () -> assertEquals(money(amount, code), result.amount()),
                () -> assertNotSame(source, result.source()),
                () -> assertNotSame(destination, result.destination()),
                () -> assertEquals(result, transfer.execute()));
    }

    @ParameterizedTest
    @CsvSource({"A,USD,USD,10,SAME_ACCOUNT", "B,VND,USD,10,CURRENCY_MISMATCH",
            "B,USD,VND,10,CURRENCY_MISMATCH", "B,USD,USD,0,NON_POSITIVE_AMOUNT",
            "B,USD,USD,100.01,INSUFFICIENT_FUNDS"})
    void GivenBusinessViolation_WhenExecuted_ThenRejectsWithoutChangingBalances(
            String destinationId, String destinationCurrency, String amountCurrency,
            String amount, TransferResult.RejectionReason reason) {
        var source = new Account("A", money("100", "USD"));
        var destination = new Account(destinationId, money("20", destinationCurrency));
        var result = new Transfer(source, destination, money(amount, amountCurrency)).execute();
        assertAll(() -> assertEquals(new TransferResult.Rejected(reason), result),
                () -> assertEquals(money("100", "USD"), source.balance()),
                () -> assertEquals(money("20", destinationCurrency), destination.balance()));
    }

    @Test
    void GivenNullTransferComponent_WhenConstructed_ThenRejectsProgrammingError() {
        var amount = money("1", "USD");
        var account = new Account("A", amount);
        assertAll(() -> assertThrows(NullPointerException.class, () -> new Transfer(null, account, amount)),
                () -> assertThrows(NullPointerException.class, () -> new Transfer(account, null, amount)),
                () -> assertThrows(NullPointerException.class, () -> new Transfer(account, account, null)));
    }
}

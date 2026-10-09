package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    private static final Currency USD = Currency.getInstance("USD");

    @ParameterizedTest
    @CsvSource({"VND,1000.00,1000", "USD,10,10.00", "USD,10.000,10.00", "USD,0,0.00", "VND,0,0"})
    void GivenExactAmount_WhenConstructed_ThenNormalizesWithoutRounding(String code, String input, String expected) {
        var money = new Money(new BigDecimal(input), Currency.getInstance(code));
        assertEquals(new BigDecimal(expected), money.amount());
    }

    @Test
    void GivenNullComponent_WhenConstructed_ThenRejects() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> new Money(null, USD)),
                () -> assertThrows(NullPointerException.class, () -> new Money(BigDecimal.ONE, null)));
    }

    @ParameterizedTest
    @CsvSource({"USD,-0.01", "VND,-1", "USD,1.001", "VND,1.1", "EUR,1"})
    void GivenInvalidAmountOrCurrency_WhenConstructed_ThenRejects(String code, String input) {
        assertThrows(IllegalArgumentException.class,
                () -> new Money(new BigDecimal(input), Currency.getInstance(code)));
    }

    @Test
    void GivenEquivalentScales_WhenCompared_ThenEqualityAndHashAgree() {
        var first = new Money(new BigDecimal("10"), USD);
        var second = new Money(new BigDecimal("10.00"), USD);
        assertAll(() -> assertEquals(first, second),
                () -> assertEquals(first.hashCode(), second.hashCode()),
                () -> assertEquals(0, first.compareTo(second)));
    }

    @Test
    void GivenDecimalAmounts_WhenAddedAndSubtracted_ThenArithmeticIsExact() {
        var first = new Money(new BigDecimal("0.10"), USD);
        var second = new Money(new BigDecimal("0.20"), USD);
        var sum = first.add(second);
        assertAll(() -> assertEquals(new BigDecimal("0.30"), sum.amount()),
                () -> assertEquals(first, sum.subtract(second)),
                () -> assertEquals(new BigDecimal("0.10"), first.amount()),
                () -> assertTrue(first.compareTo(second) < 0));
    }

    @Test
    void GivenDifferentCurrencies_WhenOperating_ThenRejects() {
        var usd = new Money(BigDecimal.ONE, USD);
        var vnd = new Money(BigDecimal.ONE, Currency.getInstance("VND"));
        assertAll(() -> assertNotEquals(usd, vnd),
                () -> assertThrows(IllegalArgumentException.class, () -> usd.add(vnd)),
                () -> assertThrows(IllegalArgumentException.class, () -> usd.subtract(vnd)),
                () -> assertThrows(IllegalArgumentException.class, () -> usd.compareTo(vnd)));
    }

    @Test
    void GivenInsufficientAmount_WhenSubtracted_ThenRejectsNegativeResult() {
        var one = new Money(BigDecimal.ONE, USD);
        var two = new Money(new BigDecimal("2"), USD);
        assertThrows(IllegalArgumentException.class, () -> one.subtract(two));
    }
}

package com.example.walletlab.wallet.domain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Read-only analytics over a deduplicated snapshot, preserving first encounter order. */
public final class TransactionAnalytics {
    private final List<Transaction> history;
    private final Map<String, Transaction> byId;

    public record AccountCurrency(String accountId, Currency currency) {
        public AccountCurrency {
            Objects.requireNonNull(accountId, "accountId");
            Objects.requireNonNull(currency, "currency");
            if (accountId.isBlank()) throw new IllegalArgumentException("Account ID must not be blank");
        }
    }

    public TransactionAnalytics(List<Transaction> transactions) {
        var snapshot = List.copyOf(transactions);
        var indexed = snapshot.stream().collect(Collectors.toMap(
                Transaction::id, Function.identity(), (first, duplicate) -> {
                    if (!first.equals(duplicate)) {
                        throw new IllegalArgumentException("Conflicting transaction ID: " + first.id());
                    }
                    return first;
                }, LinkedHashMap::new));
        history = List.copyOf(indexed.values());
        byId = Map.copyOf(indexed);
    }

    public List<Transaction> history() { return history; }

    public Set<String> transactionIds() { return byId.keySet(); }

    public Optional<Transaction> findById(String id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(byId.get(id));
    }

    public List<Transaction> successfulTransactions() {
        return history.stream().filter(t -> t.status() == Transaction.Status.SUCCESS).toList();
    }

    /** Outgoing successful volume, not account balances; never sums different currencies. */
    public Map<AccountCurrency, Money> totalsBySourceAccount() {
        var totals = history.stream().filter(t -> t.status() == Transaction.Status.SUCCESS)
                .collect(Collectors.toMap(
                        t -> new AccountCurrency(t.sourceAccountId(), t.amount().currency()),
                        Transaction::amount, Money::add));
        return Map.copyOf(totals);
    }

    public Money totalSuccessful(Currency currency) {
        var zero = new Money(BigDecimal.ZERO, currency);
        return history.stream()
                .filter(t -> t.status() == Transaction.Status.SUCCESS)
                .map(Transaction::amount)
                .filter(m -> m.currency().equals(currency))
                .reduce(zero, Money::add);
    }

    /** Ties are ordered by case-sensitive transaction ID for deterministic results. */
    public List<Transaction> topFiveSuccessful(Currency currency) {
        new Money(BigDecimal.ZERO, currency); // Validate the same supported currencies as Money.
        return history.stream()
                .filter(t -> t.status() == Transaction.Status.SUCCESS)
                .filter(t -> t.amount().currency().equals(currency))
                .sorted(Comparator.comparing((Transaction t) -> t.amount().amount()).reversed()
                        .thenComparing(Transaction::id))
                .limit(5)
                .toList();
    }
}

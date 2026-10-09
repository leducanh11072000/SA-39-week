package com.example.walletlab.wallet.domain;

import java.util.Objects;

/** Immutable snapshot, not a persistent account or an identity-based entity. */
public record Account(String id, Money balance) {
    public Account {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(balance, "balance");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Account ID must not be blank");
        }
    }
}

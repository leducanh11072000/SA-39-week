package com.example.walletlab.wallet.domain;

import java.util.Objects;

/** Immutable history row supplied by the caller; not a persisted ledger entry. */
public record Transaction(String id, String sourceAccountId, String destinationAccountId,
                          Money amount, Status status) {
    public enum Status { SUCCESS, REJECTED, FAILED }

    public Transaction {
        requireId(id);
        requireId(sourceAccountId);
        requireId(destinationAccountId);
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(status, "status");
        if (status == Status.SUCCESS
                && (amount.amount().signum() == 0 || sourceAccountId.equals(destinationAccountId))) {
            throw new IllegalArgumentException("Successful transfer must be positive and between different accounts");
        }
    }

    private static void requireId(String value) {
        Objects.requireNonNull(value, "id");
        if (value.isBlank()) throw new IllegalArgumentException("ID must not be blank");
    }
}

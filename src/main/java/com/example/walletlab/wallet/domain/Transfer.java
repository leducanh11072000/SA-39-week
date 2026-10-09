package com.example.walletlab.wallet.domain;

import java.util.Objects;

import static com.example.walletlab.wallet.domain.TransferResult.RejectionReason.*;

/** Pure calculation on snapshots. Caller owns persistence and concurrency control. */
public record Transfer(Account source, Account destination, Money amount) {
    public Transfer {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        Objects.requireNonNull(amount, "amount");
    }

    public TransferResult execute() {
        if (source.id().equals(destination.id())) {
            return new TransferResult.Rejected(SAME_ACCOUNT);
        }
        if (!source.balance().currency().equals(destination.balance().currency())
                || !source.balance().currency().equals(amount.currency())) {
            return new TransferResult.Rejected(CURRENCY_MISMATCH);
        }
        if (amount.amount().signum() == 0) {
            return new TransferResult.Rejected(NON_POSITIVE_AMOUNT);
        }
        if (source.balance().compareTo(amount) < 0) {
            return new TransferResult.Rejected(INSUFFICIENT_FUNDS);
        }
        return new TransferResult.Success(
                new Account(source.id(), source.balance().subtract(amount)),
                new Account(destination.id(), destination.balance().add(amount)), amount);
    }
}

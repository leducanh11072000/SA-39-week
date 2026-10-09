package com.example.walletlab.wallet.domain;

import java.util.Objects;

public sealed interface TransferResult
        permits TransferResult.Success, TransferResult.Rejected, TransferResult.Failed {

    record Success(Account source, Account destination, Money amount) implements TransferResult {
        public Success {
            Objects.requireNonNull(source, "source");
            Objects.requireNonNull(destination, "destination");
            Objects.requireNonNull(amount, "amount");
        }
    }

    enum RejectionReason {
        SAME_ACCOUNT, CURRENCY_MISMATCH, NON_POSITIVE_AMOUNT, INSUFFICIENT_FUNDS
    }

    record Rejected(RejectionReason reason) implements TransferResult {
        public Rejected {
            Objects.requireNonNull(reason, "reason");
        }
    }

    /** Technical failure representation; the pure domain transfer does not produce it. */
    record Failed(String code) implements TransferResult {
        public Failed {
            Objects.requireNonNull(code, "code");
            if (code.isBlank()) {
                throw new IllegalArgumentException("Failure code must not be blank");
            }
        }
    }

    default String message() {
        return switch (this) {
            case Success(var source, var destination, Money(var amount, var currency)) ->
                    "Transferred " + amount.toPlainString() + " " + currency.getCurrencyCode()
                            + " from " + source.id() + " to " + destination.id();
            case Rejected(var reason) -> "Transfer rejected: " + switch (reason) {
                case SAME_ACCOUNT -> "source and destination must differ";
                case CURRENCY_MISMATCH -> "currencies must match";
                case NON_POSITIVE_AMOUNT -> "amount must be positive";
                case INSUFFICIENT_FUNDS -> "insufficient funds";
            };
            case Failed(var code) -> "Transfer failed: " + code;
        };
    }
}

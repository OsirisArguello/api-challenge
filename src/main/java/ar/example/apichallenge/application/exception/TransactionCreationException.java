package ar.example.apichallenge.application.exception;

import lombok.Getter;

@Getter
public class TransactionCreationException extends RuntimeException {
    private String code;
    private String message;

    private TransactionCreationException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static TransactionCreationException fromTransactionAlreadyExists() {
        return new TransactionCreationException("error.transaction.id.exists", "Transaction with the given id already exists");
    }

    public static TransactionCreationException fromParentDoesntExists() {
        return new TransactionCreationException("error.transaction.parent.not.exists", "Invalid argument on the body sent");
    }
}

package com.MiniWallet.exception;

public class WalletException extends RuntimeException {
    private final ErrorCode errorCode;

    public WalletException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

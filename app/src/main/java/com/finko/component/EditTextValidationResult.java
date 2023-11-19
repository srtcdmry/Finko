package com.finko.component;

public class EditTextValidationResult {
    private final boolean isValid;
    private final String errorMessage;

    public EditTextValidationResult(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

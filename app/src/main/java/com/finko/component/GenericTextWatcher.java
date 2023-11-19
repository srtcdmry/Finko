package com.finko.component;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class GenericTextWatcher implements TextWatcher {
    private final TextInputLayout editText;
    private final EditTextValidator validator;

    public GenericTextWatcher(TextInputLayout editText, EditTextValidator validator) {
        this.editText = editText;
        this.validator = validator;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // this method is empty
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // this method is empty
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String input = editable.toString();
        EditTextValidationResult editTextValidationResult = validator.validate(input);
        if (editTextValidationResult.isValid()) {
            editText.setError(null);
        } else {
            editText.setError(editTextValidationResult.getErrorMessage());

        }
    }
}
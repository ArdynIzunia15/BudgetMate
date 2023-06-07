package com.example.expensetracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CapitalizeTextWatcher implements TextWatcher {

    private EditText editText;

    public CapitalizeTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Not used
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        if (text.isEmpty()) {
            return; // Empty text, no need for further processing
        }

        String capitalizedText = capitalizeEachWord(text);
        if (!text.equals(capitalizedText)) {
            editText.removeTextChangedListener(this);
            editText.setText(capitalizedText);
            editText.setSelection(capitalizedText.length());
            editText.addTextChangedListener(this);
        }
    }

    private String capitalizeEachWord(String text) {
        StringBuilder builder = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                c = Character.toUpperCase(c);
                capitalizeNext = false;
            }

            builder.append(c);
        }

        return builder.toString();
    }
}

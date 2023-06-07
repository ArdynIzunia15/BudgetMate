package com.example.expensetracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ThousandFormatterTextWatcher implements TextWatcher {
    private EditText editText;

    public ThousandFormatterTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not used
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not used
    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText.removeTextChangedListener(this);

        String originalText = editable.toString();

        // Remove thousand separators
        String cleanText = originalText.replace(",", "");

        // Format the number with thousand separators
        String formattedText = formatNumber(cleanText);

        // Update the EditText with the formatted text
        editText.setText(formattedText);

        if (formattedText != null) {
            editText.setSelection(formattedText.length());
        }

        editText.addTextChangedListener(this);
    }

    private String formatNumber(String number) {
        // Add thousand separators
        if(!number.equals("")){
            double parsed = Double.parseDouble(number);
            String formatted = String.format("%,.0f", parsed);

            return formatted;
        }
        else{
            return null;
        }
    }

    private String getUnformattedText(String formattedText) {
        // Remove thousand separators
        String unformattedText = formattedText.replace(",", "");

        return unformattedText;
    }
}

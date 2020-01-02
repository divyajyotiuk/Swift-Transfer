package com.codebreak.bank;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

public class EditTextWithPrefix extends TextInputEditText {

    public EditTextWithPrefix(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithPrefix);
        String prefix = attributes.getString(R.styleable.EditTextWithPrefix_prefix);
        attributes.recycle();

        if (prefix != null && !prefix.isEmpty()) {
            setCompoundDrawablesRelative(new TextDrawable(prefix+"  ", this), null, null, null);
        }

    }

    void setPrefix(String prefix)
    {
        setCompoundDrawablesRelative(new TextDrawable(prefix+"  ", this), null, null, null);

    }
}
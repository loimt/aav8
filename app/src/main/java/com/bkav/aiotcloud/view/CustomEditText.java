package com.bkav.aiotcloud.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditText extends AppCompatEditText {

public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        }

public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
        }

public interface KeyImeChange {
    void onKeyIme(int keyCode, KeyEvent event);
}

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyImeChangeListener != null) {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

    private KeyImeChange keyImeChangeListener;
}

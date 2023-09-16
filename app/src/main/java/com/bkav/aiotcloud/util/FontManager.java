package com.bkav.aiotcloud.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontManager {
    public static final String TAG = "FontManager";

    public static synchronized FontManager getInstance(Context context) {
        if (instance == null) {
            instance = new FontManager(context.getApplicationContext());
        }
        return instance;
    }

    public Typeface getDisplayMedium() {
        return displayMedium;
    }

    public Typeface getDisplaySemibold() {
        return displaySemibold;
    }

    public Typeface getDisplayThin() {
        return displayThin;
    }

    public Typeface getDisplayLight() {
        return displayLight;
    }

    public Typeface getDisplayregular() {
        return displayregular;
    }

    public Typeface getTextLight() {
        return textLight;
    }

    public Typeface getTextMedium() {
        return textMedium;
    }

    public Typeface getTextRegular() {
        return textRegular;
    }

    public Typeface getTextSemibold() {
        return textSemibold;
    }

    private static FontManager instance;
    private Typeface displayMedium;
    private Typeface displaySemibold;
    private Typeface displayThin;
    private Typeface displayLight;
    private Typeface displayregular;

    private Typeface textLight;
    private Typeface textMedium;
    private Typeface textRegular;
    private Typeface textSemibold;

    private FontManager(Context context) {
        AssetManager assetManager = context.getResources().getAssets();
        this.displayMedium = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Display-Medium.otf");
        this.displaySemibold = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Display-Semibold.otf");
        this.displayThin = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Display-Thin.otf");
        this.displayLight = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Display-Light.otf");
        this.displayregular = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Display-Regular.otf");

        this.textLight = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Text-Light.otf");
        this.textMedium = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Text-Medium.otf");
        this.textRegular = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Text-Regular.otf");
        this.textSemibold = Typeface.createFromAsset(assetManager, "fonts/SF-Pro-Text-Semibold.otf");
    }


}

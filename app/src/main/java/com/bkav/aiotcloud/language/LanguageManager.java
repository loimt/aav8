package com.bkav.aiotcloud.language;

import android.util.Log;

import com.bkav.aiotcloud.util.FileManager;
import com.bkav.aiotcloud.util.Xml;

import java.util.ArrayList;

public class LanguageManager {
    public static final int VIETNAMESE = 1;
    public static final int ENGLISH = 0;
    public static final String TAG = "LanguageManager";

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public Language getLanguage(int index) {
        return this.languageList.get(index);
    }

    public String getValue(String key) {
        if (currentLanguage == null) {
            return "";
        }
        return currentLanguage.getValue(key);
    }

    public ArrayList<Language> getLanguageList() {
        return languageList;
    }

    public void changeLanguage(Language language) {
        if (currentLanguage == language) {
            return;
        }
        if (currentLanguage != null) {
            currentLanguage.setDefault(false);
        }
        currentLanguage = language;
        currentLanguage.setDefault(true);
    }

    private static LanguageManager instance;
    private ArrayList<Language> languageList;
    private Language currentLanguage = null;

    private LanguageManager() {
        this.create();
    }

    private void create() {
        this.languageList = new ArrayList<>();
        Xml languages = FileManager.loadXmlFromHome(
                "language/language.xml");
        if (languages == null) {
            return;
        }
        Xml languageXml = languages.getChild("language");
        while (languageXml != null) {
            Language language = new Language(languageXml);
            this.languageList.add(language);
            if (language.isDefault()) {
                currentLanguage = language;
            }
            languageXml = languageXml.getNext("language");
        }
        Log.e("s", languageList.size() + " ");
        if (currentLanguage == null) {
            currentLanguage = languageList.get(0);
        }
    }
}
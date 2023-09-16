package com.bkav.aiotcloud.language;


import com.bkav.aiotcloud.util.FileManager;
import com.bkav.aiotcloud.util.Xml;

import java.util.Hashtable;

public class Language {

    public Language(Xml xml) {
        this.value = xml.getAttrib("value");
        this.source = xml.getAttrib("source");
        isDefault = false;
        try {
            this.isDefault = Integer.parseInt(xml.getAttrib("default")) == 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        this.items = new Hashtable<>();
        if (isDefault) {
            this.loadSource();
        }

        this.isLoaded = false;
    }

    public String getValue() {
        return value;
    }

    public String getSource() {
        return source;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
        if (!isLoaded && this.isDefault) {
            this.loadSource();
        }
    }

    public String getValue(String key) {
        if (items.containsKey(key)) {
            LangugeItem langugeItem = items.get(key);
            if (langugeItem != null) {
                return langugeItem.getVaule();
            }
        }
        return "NULL";
    }

    @Override
    public String toString() {
        return value;
    }

    private boolean isLoaded;
    private boolean isDefault;
    private final String value;
    private final String source;
    private final Hashtable<String, LangugeItem> items;

    private void loadSource() {
        isLoaded = true;
        Xml language = FileManager.loadXmlFromHome("language/" + source);
        if (language == null) {
            return;
        }

        Xml item = language.getChild("item");
        while (item != null) {
            LangugeItem langugeItem = new LangugeItem(item);
            if (!items.containsKey(langugeItem.getKey())) {
                items.put(langugeItem.getKey(), langugeItem);
            }
            item = item.getNext("item");
        }
    }
}
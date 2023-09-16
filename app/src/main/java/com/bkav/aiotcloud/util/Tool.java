package com.bkav.aiotcloud.util;

import java.util.Arrays;

public class Tool {
    public static String getStringWithoutFirstEle(String content, String splitRegex, String joinRegex){
        String[] arrayContent = content.split(splitRegex);
        String[] modifiedArray = Arrays.copyOfRange(arrayContent, 1, arrayContent.length);
        return String.join(joinRegex, modifiedArray).trim();
    }

}

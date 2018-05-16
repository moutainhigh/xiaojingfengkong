package com.shangdao.phoenix.util;

public class MethodNameFormatUtil {

    public static String formatName(String methodName) {
        char[] ch = methodName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (i == 5) {
                ch[i] = (char)((int)ch[i] - 32);
            }
        }
        return new String(ch);
    }

}

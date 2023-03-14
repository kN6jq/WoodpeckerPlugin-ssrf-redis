package me.gv7.woodpecker.plugin;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String hexToString(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            String hex = Integer.toHexString(data.charAt(i));
            sb.append("\\x");
            sb.append(hex.length() < 2 ? "0" : "");
            sb.append(hex);
        }
        return sb.toString();
    }
    public static String decodeHex(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '\\' && i + 3 < data.length() && data.charAt(i + 1) == 'x') {
                int hexValue = Integer.parseInt(data.substring(i + 2, i + 4), 16);
                sb.append((char) hexValue);
                i += 3;
            } else {
                sb.append(data.charAt(i));
            }
        }
        return sb.toString();
    }


}

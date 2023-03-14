package me.gv7.woodpecker.plugin;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GopherCodec {

    public static String ord2hex(char c) {
        return "%" + String.format("%02X", (int) c);
    }

    public static String encode4gopher(List<String> command, String redisHost, String redisPort) {
        String ret = "gopher://" + redisHost + ":" + redisPort + "/_";
        for (String line : command) {
            List<String> redis_resps = new ArrayList<String>();
            boolean str_flag = false;
            StringBuilder word = new StringBuilder();
            for (char charactor : line.toCharArray()) {
                if (str_flag == true) {
                    if (charactor == '"' || charactor == '\'') {
                        str_flag = false;
                        if (word.length() != 0) {
                            word = new StringBuilder(word.toString().replace("\\n", "\n"));
                            redis_resps.add(word.toString());
                        }
                        word = new StringBuilder();
                    } else {
                        word.append(charactor);
                    }
                } else if (word.length() == 0 && (charactor == '"' || charactor == '\'')) {
                    str_flag = true;
                } else {
                    if (charactor == ' ') {
                        if (word.length() != 0) {
                            redis_resps.add(word.toString());
                        }
                        word = new StringBuilder();
                    } else if (charactor == '\n') {
                        if (word.length() != 0) {
                            redis_resps.add(word.toString());
                        }
                        word = new StringBuilder();
                    } else {
                        word.append(charactor);
                    }
                }
            }
            String tmp_line = "*" + redis_resps.size() + "\r\n";
            for (String cmd : redis_resps) {
                tmp_line += "$" + cmd.length() + "\r\n" + cmd + "\r\n";
            }
            for (char ch : tmp_line.toCharArray()) {
                ret += ord2hex(ch);
            }
        }
        return ret;
    }






}

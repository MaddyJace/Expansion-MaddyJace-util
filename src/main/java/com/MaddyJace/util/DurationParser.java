package com.MaddyJace.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {

    public static long parseToDays(String input) {

        if (input == null || input.trim().isEmpty()) {
            return -1;
        }
        Pattern p = Pattern.compile("(\\d+)\\s*(y|mo|w|d|h|m|s)");
        Matcher m = p.matcher(input);
        long totalSeconds = 0;
        boolean found = false;
        while (m.find()) {
            found = true;
            long value = Long.parseLong(m.group(1));
            String unit = m.group(2);
            switch (unit) {
                case "y":  totalSeconds += value * 365L * 24 * 3600; break;
                case "mo": totalSeconds += value * 30L  * 24 * 3600; break;
                case "w":  totalSeconds += value * 7L   * 24 * 3600; break;
                case "d":  totalSeconds += value * 24L  * 3600; break;
                case "h":  totalSeconds += value * 3600L; break;
                case "m":  totalSeconds += value * 60L; break;
                case "s":  totalSeconds += value; break;
            }
        }
        if (!found) {
            return -1;
        }
        return Math.round(totalSeconds / 86400.0);
    }

}

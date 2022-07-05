package br.com.erudio.converters;

import java.util.Objects;

public class NumberConverter {

    public static boolean isNotNumeric(String value) {
        if (Objects.isNull(value)) {
            return true;
        }

        var strNumber = getFormatted(value);

        var isNumeric = strNumber.matches("[-+]?\\d*\\.?\\d+");

        return !isNumeric;
    }

    public static Double convertToDouble(String value) {
        return Double.parseDouble(getFormatted(value));
    }

    private static String getFormatted(String value) {
        return value.replace(",", ".");
    }
}

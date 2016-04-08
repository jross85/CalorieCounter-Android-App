package util;

import java.text.DecimalFormat;

/**
 * Created by ross on 4/7/16.
 */
public class Util {

    public static String formatNumber(int value) {

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formatted = formatter.format(value);

        return formatted;
    }
}

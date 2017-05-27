package util;

import java.util.regex.Pattern;

/**
 * Created by daile on 2017/4/16.
 */
public class UrlValidateUtil {

    static String urlRegex = "^(https?|ftp|file)://.+$";

    static Pattern urlPattern = Pattern.compile(urlRegex);

    public static boolean validateUrl(String url) {
        return urlPattern.matcher(url).matches();
    }
}

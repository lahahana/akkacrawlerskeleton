package util;

import okhttp3.Request;

/**
 * Created by daile on 2017/5/7.
 */
public class ReqFaker {

    public static Request enrichUserAgent(Request req) {
        req.newBuilder().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36");
        return req;
    }
}

package team_wallstreet.wallstreet.HttpReq;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import team_wallstreet.wallstreet.R;

/**
 * Created by alant on 5/20/2017.
 */

public class CookieHelper {

    public static final String cookie_fname = "cookie.txt";
    private String cookie;
    private String crumb;

    public void getCookie(final Context context){
        final RequestManager requestManager = new RequestManager();
        requestManager.makeRequest(context, "https://finance.yahoo.com/quote/AC?p=AC", new RequestListener() {

            @Override
            public void onRequestComplete(final String response) {

                int index = response.lastIndexOf("CrumbStore");
                if (index != -1) {
                    Log.e("CRUMB", "found crumb: " + response.substring(index+22, index+35));
                    Log.e("COOKIE", "found cookie: " + requestManager.cookieManager.getCookieStore().getCookies().get(0).toString());


                    // Save cookie & crumb to var
                    crumb = response.substring(index+22, index+33);
                    cookie = requestManager.cookieManager.getCookieStore().getCookies().get(0).toString();
//                    StringEscapeUtils.unescapeJava(response.substring(index+22, index+33));
                }
            }
        },null);
    }

    public String getCookie(){
        return cookie;
    }

    public String getCrumb() {
        return crumb;
    }

}

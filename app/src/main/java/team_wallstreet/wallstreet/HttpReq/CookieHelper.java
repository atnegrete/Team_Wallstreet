package team_wallstreet.wallstreet.HttpReq;

import android.content.Context;

/**
 * Created by alant on 5/20/2017.
 */

public class CookieHelper {

    private String cookie;
    private String crumb;

    public void getCookie(final Context context, final RequestManager requestManager, RequestListener requestListener){
        requestManager.makeRequest(context, "https://finance.yahoo.com/quote/AC?p=AC", requestListener, null);
    }

    public String getCookie(){
        return cookie;
    }

    public String getCrumb() {
        return crumb;
    }

    public void setCookie(String cookie) { this.cookie = cookie; }
    public void setCrumb(String crumb) { this.crumb = crumb; }

}

package team_wallstreet.wallstreet.HttpReq;

import android.content.Context;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by alant on 5/19/2017.
 */

public class RequestManager {

    private OkHttpClient client;
    private Request request;
    private RequestListener listener;
    CookieManager cookieManager;
    public static String RESULT_KEY = "results_key";

    public RequestManager(){
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }

    public void makeRequest(final Context context, String url, final RequestListener listener, String cookie){
        this.listener = listener;
        if(cookie == null)
            request = new Request.Builder().url(url).build();
        else
            request = new Request.Builder().url(url).addHeader("cookie",cookie).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onRequestComplete(e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                listener.onRequestComplete(myResponse);



//                Intent intent = new Intent("SEARCH_FILTER");
//                intent.putExtra(RESULT_KEY, "myfile.txt");
//                context.sendBroadcast(intent);
//
//                try {
//                    OutputStreamWriter outputStream = new OutputStreamWriter(context.openFileOutput("myfile.txt", Context.MODE_PRIVATE));
//                    outputStream.write(myResponse);
//                    outputStream.close();
//                } catch (Exception e) {
//                    Log.e("Exception", "File write failed: " + e.toString());
//                }
//
//                Log.e("RESPONSE", ""+ myResponse.getBytes("UTF-8").length);
            }
        });
    }
}

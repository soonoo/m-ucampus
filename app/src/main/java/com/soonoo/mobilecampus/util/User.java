package com.soonoo.mobilecampus.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.soonoo.mobilecampus.Sites;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by soonoo on 2015-02-15.
 */
public class User {
    public static String cookie = "";
    public static ArrayList<String> subCode;
    public static ArrayList<String> subName;
    public static ArrayList<Boolean> isNew;
    public static Context context;

    User(Context context){
        this.context = context;
    }

    public static String setCookie(HttpURLConnection con) {
        String headerName;
        String cookie;
        StringBuilder cookies = new StringBuilder();

        for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {
                cookie = con.getHeaderField(i);

                if (cookie.contains("ccmedia")) continue;

                cookie = cookie.substring(0, cookie.indexOf(";") + 1);
                cookies.append(cookie);
                cookies.append(" ");
            }
        }
        return cookies.toString();
    }


    public static boolean login(String id, String pw) {
        String loginQuery = Sites.LOGIN_QUERY + "&member_no=" + id + "&password=" + pw;
        HttpsURLConnection con;

        try {
            con = (HttpsURLConnection) new URL(Sites.LOGIN_URL).openConnection();

            // https://evilzone.org/java/(java)-how-can-i-add-a-ciphersuite/
            // IMPORTANT
           // SSLSocketFactoryEx factory = new SSLSocketFactoryEx();
            //con.setSSLSocketFactory(factory);
            //con.setRequestProperty("charset", "UTF-8");

            con.setRequestMethod("POST");
            //con.setRequestProperty("Content-length", String.valueOf(loginQuery.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           // con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)");
            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(loginQuery);
            output.close();

            if (isValid(con)) {
                cookie = setCookie(con);
                return true;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
        return false;
    }

    public static void getSession() {
        try {
            //URL url = new URL(MyUrl.SESSION_URL);
            HttpURLConnection con = (HttpURLConnection) new URL(Sites.SESSION_URL).openConnection();

            con.setRequestMethod("GET");
            //con.setRequestProperty("Content-length", String.valueOf(qq.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            con.setRequestProperty("Cookie", User.cookie);

            User.cookie += setCookie(con);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
    }

    static boolean isValid(HttpURLConnection con) {
        String headerName;
        String cookie;
        StringBuilder cookies = new StringBuilder();

        for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {
                cookie = con.getHeaderField(i);
                if (cookie.contains("deleted")) return false;
                else return true;
            }
        }
        return true;
    }

    // POST
    public static String getHtml(String method, String url, String query, String encoding) throws Exception {
        HttpURLConnection con;
        DataOutputStream output;
        String line = null;
        StringBuilder result = new StringBuilder();

        if(User.cookie == null || User.cookie.equals("")) throw new Exception();

        try {
            con = (HttpURLConnection) new URL(url).openConnection();

            /*con.setConnectTimeout(5000);
            con.setReadTimeout(5000);*/

            con.setRequestMethod(method);
            con.setRequestProperty("Content-length", String.valueOf(query.length()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");
            con.setRequestProperty("User-Agent", "soonoo's android client");
            con.setRequestProperty("Cookie", User.cookie);
            con.setDoInput(true);
            con.setDoOutput(true);

            output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(query);
            output.close();

            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, encoding));

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
        return result.toString();
    }

    // GET
    public static String getHtml(String method, String url, String encoding)  throws Exception {
        HttpURLConnection con;
        String line;
        StringBuilder result = new StringBuilder();

        if(User.cookie == null || User.cookie.equals("")) throw new Exception();

        try {
            con = (HttpURLConnection) new URL(url).openConnection();

            /*con.setConnectTimeout(5000);
            con.setReadTimeout(5000);*/

            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0");
            con.setRequestProperty("User-Agent", "soonoo's android client");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cookie", User.cookie);

            InputStream is = con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, encoding));

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Log.e("INFO", exceptionAsStrting);
            e.printStackTrace();
        }
        return result.toString();
    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager cManager;
        NetworkInfo mobile;
        NetworkInfo wifi;

        cManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        } else {
            return false;
        }

    }
}

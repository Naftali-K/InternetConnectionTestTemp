package com.nk.internetconnectiontesttemp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * @Author: naftalikomarovski
 * @Date: 2024/10/08
 */

public class PublicMethods {
    private static final String TAG = "Test_code";

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d(TAG, "checkConnection1: Connection is by WIFI");
            Toast.makeText(context, "Connection is by WIFI", Toast.LENGTH_SHORT).show();
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            Log.d(TAG, "checkConnection1: Connection is by MOBILE");
            Toast.makeText(context, "Connection is by MOBILE", Toast.LENGTH_SHORT).show();
        }

        return networkInfo.isConnected();
    }

    public static boolean InternetIsConnected() {
        try {
//            String command = "ping -c 1 google.com"; // Have bug with this address.
            String command = "ping -c 1 8.8.8.8";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}

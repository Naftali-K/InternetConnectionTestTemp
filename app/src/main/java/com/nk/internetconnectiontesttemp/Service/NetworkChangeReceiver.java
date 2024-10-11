package com.nk.internetconnectiontesttemp.Service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.nk.internetconnectiontesttemp.R;
import com.nk.internetconnectiontesttemp.SecondActivity;

/**
 * @Author: naftalikomarovski
 * @Date: 2024/10/08
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "Test_code";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "onReceive: NetworkChangeReceiver BroadcastReceiver ACTIVE");

        String status = NetworkUtil.getConnectivityStatusString(context);
        boolean internetConnected = !NetworkUtil.InternetIsConnected();

        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: NetworkChangeReceiver Status: " + status + "\nInternet Connection: " + internetConnected);

        if (internetConnected) {
//            Intent intent1 = new Intent(context, SecondActivity.class);
//            context.startActivity(intent1);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Disconnect");
                builder.setMessage("Happened disconnection. Try reconnect.");
//                builder.setIcon(R.drawable.icon_cell_wifi);
                builder.setNegativeButton("OK", new  DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // クリックしたときの処理

                    }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}

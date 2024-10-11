package com.nk.internetconnectiontesttemp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nk.internetconnectiontesttemp.Service.NetworkChangeReceiver;

import java.io.IOException;

/**
 * https://youtu.be/pKCWvRyfqdc?si=fJ8-IpkFrAvpzwW- - lesson video
 * https://www.repeato.app/how-to-check-internet-access-on-android-effectively/
 * https://www.viralpatel.net/android-internet-connection-status-network-change/ - check Internet connection automatically
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";
    private TextView statusConnectionTextView, statusInternetConnectionTextView,
            staticStatusInternetConnectionTextView;
    private Button connectionCheckBtn, internetConnectionCheckBtn, staticInternetConnectionCheckBtn,
            secondActivityBtn;

    private NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setReferences();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerReceivers();

        connectionCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusConnectionTextView(checkConnection1());
            }
        });

        internetConnectionCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusInternetConnectionTextView(InternetIsConnected());
//                setStatusInternetConnectionTextView(isOnline());
            }
        });

        staticInternetConnectionCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PublicMethods.InternetIsConnected()) {
                    staticStatusInternetConnectionTextView.setTextAppearance(R.style.connected_style);
                    staticStatusInternetConnectionTextView.setText("Internet Connected");
                } else {
                    staticStatusInternetConnectionTextView.setTextAppearance(R.style.disconnect_style);
                    staticStatusInternetConnectionTextView.setText("Internet Disconnected");
                }
            }
        });

        secondActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SecondActivity.class));
            }
        });
    }

    private void setReferences() {
        statusConnectionTextView = findViewById(R.id.status_connection_text_view);
        statusInternetConnectionTextView = findViewById(R.id.status_internet_connection_text_view);
        staticStatusInternetConnectionTextView = findViewById(R.id.static_status_internet_connection_text_view);
        connectionCheckBtn = findViewById(R.id.connection_check_btn);
        internetConnectionCheckBtn = findViewById(R.id.internet_connection_check_btn);
        staticInternetConnectionCheckBtn = findViewById(R.id.static_internet_connection_check_btn);
        secondActivityBtn = findViewById(R.id.second_activity_btn);
    }

    private void registerReceivers() {
        receiver = new NetworkChangeReceiver();

        IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        registerReceiver(receiver, filter);
    }

    private boolean checkConnection1() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d(TAG, "checkConnection1: Connection is by WIFI");
            Toast.makeText(this, "Connection is by WIFI", Toast.LENGTH_SHORT).show();
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            Log.d(TAG, "checkConnection1: Connection is by MOBILE");
            Toast.makeText(this, "Connection is by MOBILE", Toast.LENGTH_SHORT).show();
        }

        return networkInfo.isConnected();
    }

    private boolean checkConnection2() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean checkConnection3() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public boolean InternetIsConnected() {
        try {
//            String command = "ping -c 1 google.com"; // Have bug with this address.
            String command = "ping -c 1 8.8.8.8";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setStatusConnectionTextView(boolean connection) {
        if (connection) {
            statusConnectionTextView.setTextAppearance(R.style.connected_style);
            statusConnectionTextView.setText("Connected");
        } else {
            statusConnectionTextView.setTextAppearance(R.style.disconnect_style);
            statusConnectionTextView.setText("Disconnect");
        }
    }

    private void setStatusInternetConnectionTextView(boolean connection) {
        if (connection) {
            statusInternetConnectionTextView.setTextAppearance(R.style.connected_style);
            statusInternetConnectionTextView.setText("Internet Connected");
        } else {
            statusInternetConnectionTextView.setTextAppearance(R.style.disconnect_style);
            statusInternetConnectionTextView.setText("Internet Disconnected");

            openDialogNoInternet();
        }
    }

    private void openDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection");
            builder.setMessage("Place connect to the Internet to process.");
            builder.setPositiveButton("Open WIFI Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
//            builder.create().show();

        AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }
}
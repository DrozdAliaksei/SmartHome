package com.example.smarthome;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationActivity extends AppCompatActivity {
    private static final String TAG = "AuthorizationActivity";

    SharedPreferences sPref;
    private Pattern pattern;
    private Matcher matcher;
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private Button autorization;
    private ImageButton connectingSettings;
    private TextInputLayout login;
    private TextInputLayout password;
    private TextInputEditText editLogin;
    private TextInputEditText editPassword;


    final String SAVED_IP = "saved_ip";
    final String SAVED_PORT = "saved_port";
    private EditText ipAddress;
    private EditText portNum;

    final Context context = this;

    String ip;
    String port;

    String log;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        setContentView(R.layout.authorization);

        getConnectingSettings();

        autorization = findViewById(R.id.autorization);
        connectingSettings = findViewById(R.id.connectionSettings);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        editLogin = findViewById(R.id.editLogin);
        editPassword = findViewById(R.id.editPassword);
        ipAddress = findViewById(R.id.ip_address);
        portNum = findViewById(R.id.port_num);

        autorization.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean ipcheck;
                boolean portcheck;

                getConnectingSettings();
                if (!ip.isEmpty()) {
                    ipcheck = checkIP(ip);
                    Log.i(TAG,"ip: " + ip);
                }else {
                    ipcheck = false;
                    Log.i(TAG,"Smth wrong with ip - " + ip);
                }

                if(!port.isEmpty() && (Integer.parseInt(port) < 65535 || Integer.parseInt(port) > 0)){
                    portcheck = true;
                    Log.i(TAG,"port: " + port);
                }else {
                    portcheck = false;
                    Log.i(TAG,"Smth wrong with port - " + port);
                }

                if (ipcheck && portcheck) {

                    connectionStatus(Communication.connection(ip, port));

                } else {
                    Toast.makeText(getApplicationContext(), "Connection impossible\nCheck connection data", Toast.LENGTH_LONG).show();
                }
            }
        });
        connectingSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ConnectionService Settings dialog opening");
                // get connecting_settings.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View settingsView = li.inflate(R.layout.connecting_settings, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alert dialog builder
                alertDialogBuilder.setView(settingsView);

                ipAddress = settingsView.findViewById(R.id.ip_address);
                portNum = settingsView.findViewById(R.id.port_num);

                loadSettings();

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //saving in memory preferences
                                        saveSettings();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.i(TAG, "Setting dialog closed");
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    private void connectionStatus(Boolean data) {
        Log.i(TAG, "ConnectionStatus inside function - status: " + data);
        if (data) {
            Log.i(TAG, "Connection done");
            Intent intent = new Intent(AuthorizationActivity.this, ControlPanel.class);
            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            startActivity(intent);

        } else {
            AuthorizationActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connection failed\ncheck connection data\nand try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void saveSettings() {
        Log.i(TAG, "Saving data");
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_IP, ipAddress.getText().toString());
        ed.putString(SAVED_PORT, portNum.getText().toString());
        Log.i(TAG, "Data:" + ipAddress.getText().toString() + "_" + portNum.getText().toString());
        ed.apply();
    }

    void loadSettings() {
        Log.i(TAG, "Loading saved before data");
        sPref = getPreferences(MODE_PRIVATE);
        String savedIp = sPref.getString(SAVED_IP, "");
        String savedPort = sPref.getString(SAVED_PORT, "");
        ipAddress.setText(savedIp);
        portNum.setText(savedPort);
    }

    void getConnectingSettings() {
        sPref = getPreferences(MODE_PRIVATE);
        ip = sPref.getString(SAVED_IP, "");
        port = sPref.getString(SAVED_PORT, "");
    }

    public boolean checkIP(final String ip) {
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

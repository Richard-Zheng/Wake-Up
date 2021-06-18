package com.richard.wakeup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends Activity {

    private EditText hostnameEdit;
    private EditText portEdit;
    private EditText macAddressEdit;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostnameEdit = (EditText) findViewById(R.id.edit_host);
        portEdit = (EditText) findViewById(R.id.edit_port);
        macAddressEdit = (EditText) findViewById(R.id.edit_mac_address);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        hostnameEdit.setText(sharedPref.getString("hostname", "192.168.1.255"));
        portEdit.setText(sharedPref.getString("port", "9"));
        macAddressEdit.setText(sharedPref.getString("macAddress", "00:00:00:00:00:00"));
    }

    public void onWake(View view) {
        String hostname = hostnameEdit.getText().toString();
        String port = portEdit.getText().toString();
        String macAddress = macAddressEdit.getText().toString().replace(":", "");

        try {
            WolTask wolTask = new WolTask();
            wolTask.execute(hostname, port, macAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSave(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("hostname", hostnameEdit.getText().toString());
        editor.putString("port", portEdit.getText().toString());
        editor.putString("macAddress", macAddressEdit.getText().toString());
        editor.apply();
    }

    @SuppressWarnings("deprecation")
    private static class WolTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            try {
                WolPacket wolPacket = new WolPacket(args[0], Integer.parseInt(args[1]), args[2]);
                wolPacket.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
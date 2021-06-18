package com.richard.wakeup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onWake(View view) {
        String hostname = ((EditText) findViewById(R.id.edit_host)).getText().toString();
        String port = ((EditText) findViewById(R.id.edit_port)).getText().toString();
        String macAddress = ((EditText) findViewById(R.id.edit_mac_address)).getText().toString().replace(":", "");

        try {
            WolTask wolTask = new WolTask();
            wolTask.execute(hostname, port, macAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
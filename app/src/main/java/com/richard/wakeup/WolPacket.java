package com.richard.wakeup;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class WolPacket {

    private static final String COMMON_HEAD = "FFFFFFFFFFFF";

    private byte[] magicBytes;
    private InetSocketAddress target;

    public WolPacket(String host, int port, String macAddress) throws UnknownHostException {
        this.target = new InetSocketAddress(host, port);

        String magicString = "";
        for (int i = 1; i <= 16; i++) {
            magicString = magicString + macAddress;
        }
        magicString = COMMON_HEAD + magicString;
        Log.d("WolPacket", magicString);
        this.magicBytes = BytesUtil.hexStringToBytes(magicString);
    }

    public void send() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(this.magicBytes, this.magicBytes.length, this.target);
        socket.send(packet);
        socket.close();
    }
}

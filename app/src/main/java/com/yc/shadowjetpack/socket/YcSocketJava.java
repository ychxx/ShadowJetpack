package com.yc.shadowjetpack.socket;

import android.util.Log;

import com.yc.jetpacklib.extension.YcLogExtKt;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Creator: yc
 * Date: 2021/10/11 14:43
 * UseDes:
 */

public class YcSocketJava {
    Socket mSocket;
    public static char SOH = (char) Integer.parseInt("1");

    void create(String ip, int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(ip, port);
                    if (mSocket.isConnected()) {
                        receive();
                    }
                } catch (IOException e) {
                    Log.e("Socket", "连接出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void receive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("Socket", "收到数据开始");
                    InputStream inputStream = mSocket.getInputStream();
                    int dataSize = 0;
                    byte[] buffers = new byte[1024];

                    while ((dataSize = inputStream.read(buffers)) != -1) {
                        byte[] realData = new byte[dataSize];
                        System.arraycopy(buffers, 0, realData, 0, dataSize);
                        Log.e("Socket", "收到数据：" + new String(realData));
                    }
                    Log.e("Socket", "收到数据结束");
                } catch (IOException e) {
                    Log.e("Socket", "收到数据出错");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void send(String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Socket", "发送的数据：" + data);
                try {
                    mSocket.getOutputStream().write(data.getBytes());
                } catch (IOException e) {
                    Log.e("Socket", "发送数据出错");
                    e.printStackTrace();
                }
                Log.e("Socket", "发送数据结束");
            }
        }).start();
    }
}

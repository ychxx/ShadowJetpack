package com.yc.shadowjetpack.socket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import com.yc.jetpacklib.extension.YcLogExtKt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creator: yc
 * Date: 2021/10/11 15:27
 * UseDes:
 */
@SuppressLint("HandlerLeak")
public class SocketServer {
    //服务器端口
    public final static int SERVERPORT = 5111;

    public static String getLocalIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        // Log.d(Tag, "int ip "+ipAddress);
        if (ipAddress == 0) return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    private ServerSocket mServerSocket;
    //存储所有客户端Socket连接对象
    public List<Socket> mClientList = new ArrayList<>();

    public void startServer() {
        try {
            mServerSocket = new ServerSocket(SERVERPORT);
            Log.e("socket服务端", "创建服务端成功");
            startAccept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClient() {
        for (Socket client : mClientList) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mClientList.clear();
    }

    private void startAccept() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket client = mServerSocket.accept();
                        Log.e("socket服务端", "收到一个连接：" + client);
                        mClientList.add(client);
                        createClient(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void createClient(Socket client) throws IOException {
        InputStream im = client.getInputStream();
        OutputStream om = client.getOutputStream();
        om.write("服务端数据：连接成功".getBytes());
        byte[] buffers = new byte[1024];
        int length = 0;
        byte[] realData = null;
        while ((length = im.read(buffers)) != -1) {
            realData = new byte[length];
            System.arraycopy(buffers, 0, realData, 0, length);
            //这里可以写向服务端发信息的逻辑
            Log.e("socket服务端", "收到数据" + new String(realData));
        }
    }
}

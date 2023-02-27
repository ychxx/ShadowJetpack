//package com.yc.shadowjetpack.socket
//
//import android.annotation.SuppressLint
//import android.util.Log
//import okhttp3.OkHttpClient
//import java.net.URI
//import javax.net.ssl.*
//
///**
// * Creator: yc
// * Date: 2022/11/21 20:54
// * UseDes:
// */
//class WebSocketCore(host: String, timeout: Int, private val webSocketListener: WebSocketListener?) :
//    WebSocketClient(URI(host), object : Draft_6455() {}, null, timeout) {
//    init {
//        val builder = OkHttpClient.Builder()
//        val factory: SSLSocketFactory = RxUtils.createSSLSocketFactory()
//        builder.sslSocketFactory(factory, TrustAllCerts())
//        builder.hostnameVerifier(RxUtils.TrustAllHostnameVerifier())
//        val okHttpClient = builder.build()
//        socket = okHttpClient.sslSocketFactory.createSocket()
//    }
//
//    override fun onOpen(handshakedata: ServerHandshake?) {
//        webSocketListener?.onOpen()
//    }
//
//    override fun onClose(code: Int, reason: String?, remote: Boolean) {
//        if (!isClose) {
//            Log.d("重连机制", "close链接出错，准备重连")
//        } else isClose = false
//    }
//
//    override fun onMessage(message: String?) {
//        webSocketListener?.onMessage(message)
//    }
//
//    override fun onError(ex: Exception?) {
//        webSocketListener?.onError()
//    }
//}
//
//
////工具类
//public class RxUtils {
//    @SuppressLint("TrulyRandom")
//    public static SSLSocketFactory createSSLSocketFactory() {
//
//        SSLSocketFactory sSLSocketFactory = null;
//
//        try {
//
//            SSLContext sc = SSLContext . getInstance ("TLS");
//
//            sc.init(
//                null, new TrustManager []{ new TrustAllManager () },
//
//                new SecureRandom ()
//            );
//
//            sSLSocketFactory = sc.getSocketFactory();
//
//        } catch (Exception ignored) {
//
//        }
//
//        return sSLSocketFactory;
//
//    }
//
//    public static class TrustAllManager implements X509TrustManager {
//        @SuppressLint("TrustAllX509TrustManager")
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType)
//
//        throws CertificateException {
//
//        }
//
//        @SuppressLint("TrustAllX509TrustManager")
//
//        @Override
//
//        public void checkServerTrusted(X509Certificate[] chain, String authType)
//        throws CertificateException {
//        }
//        @Override
//        public X509Certificate [] getAcceptedIssuers () {
//            return new X509Certificate [0];
//        }
//    }
//    public static class TrustAllHostnameVerifier : HostnameVerifier {
//        @SuppressLint("BadHostnameVerifier")
//        @Override
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }
//
//}
//
//class TrustAllCerts : X509TrustManager {
//    @SuppressLint("TrustAllX509TrustManager")
//    override fun checkClientTrusted(chain: Array, authType: String) {
//    }
//    @SuppressLint("TrustAllX509TrustManager")
//
//    override fun checkServerTrusted(chain: Array, authType: String) {}
//    override fun getAcceptedIssuers(): Array {
//        return arrayOfNulls(0)
//    }
//}
////管理类
//class WebSocketManager {
////0标志禁止心跳
//    private val heart = 0
//    private val timeout = 10000
//    private var webSocketListener: WebSocketListener? = null
//    var webSocketCore: WebSocketCore? = null
//    companion object {
//        var isClose = false
//        val instance by lazy { WebSocketManager() }
//    }
//    fun setListener(webSocketListener: WebSocketListener?) {
//        this.webSocketListener = webSocketListener
//    }
//    private fun getClient(): WebSocketCore {
//        if (webSocketCore == null) {
//            val client by lazy { WebSocketCore(SOCKET_HOST_INFO, timeout, webSocketListener) }
//            this.webSocketCore = client
//        }
//        return this.webSocketCore!!
//    }
//    fun connect() = getClient().apply { connectionLostTimeout = heart }.connect()
//    fun sendMessage(msg: String) {
//        if (getClient().isOpen) {
//            getClient().send(msg)
//        }
//    }
//    fun close() {
//        isClose = true
//        getClient().close()
//    }
//
//}
//
////监听类
//interface WebSocketListener {
//    fun onOpen()
//    fun onMessage(message: String?)
//    fun onError()
//}

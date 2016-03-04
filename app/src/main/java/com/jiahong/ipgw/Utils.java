package com.jiahong.ipgw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dd.CircularProgressButton;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by xjh96 on 2015/10/14.
 */
public class Utils {
    private static boolean exist = false;
    private static String username;
    private static String password;
    private static String operation;
    public static int selectedID = -1;
    public static Handler utilsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message n = new Message();
            if (thread_connect != null) {
                thread_connect.interrupt();
                thread_connect = null;
                exist = false;
            }
            if (thread_overtime != null){
                thread_overtime.interrupt();
                thread_overtime = null;
            }
            switch (msg.what) {
                case 0://取消连接
                    n.what = 2;
                    break;
                case 1://连接失败
                    n.what = 0;
                    break;
                case 2://连接成功
                    n.what = 3;
                    break;
            }
            fragment_Connection.handlerConnection.obtainMessage();
            fragment_Connection.handlerConnection.sendMessage(n);
        }
    };

    public static Thread thread_connect;
    public static Thread thread_overtime;
    public static void NetworkAction(final String username, final String password, final String operation) {
        /*if (isNetworkConnected(MainActivity.context)){
            MainActivity.MSG = MainActivity.context.getString()
        }*/
        Utils.username = username;
        Utils.password = password;
        Utils.operation = operation;

        thread_connect = new Thread(new Runnable() {
            @Override
            public void run() {
                Message m;
                try {
                    exist = true;
                    initTrustSSL();
                    String html = HttpRequest.get("https://ipgw.neu.edu.cn/ipgw/ipgw.ipgw?uid=" + username
                            + "&password=" + password
                            + "&range=2&operation="
                            + operation + "&timeout=1")
                            .body();
/*                    Map<String, String> map = new HashMap<String, String>();
                    map.put("uid","20144786");
                    map.put("password","19960614");
                    String post = HttpRequest.post("http://jifei.neu.edu.cn/stats/dashboard?sid=17420134611406892", map, true).body() + "a";
                    System.out.println(post);*/
//                    System.out.println("Response was: " + html);
                    String str[] = {"口令错误", "无法登陆", "您预设的", "请输入", "Connect successfully", "Disconnect All Succeeded", "Disconnect Succeeded"};
                    for (int i = 0; i < str.length; i++) {
                        if (html.indexOf(str[i]) != -1) {
                            exist = false;
                            switch (i) {
                                case 0:
                                    MainActivity.MSG = MainActivity.context.getString(R.string.WrongPassword);
                                    m = new Message();
                                    m.what = 0;
                                    fragment_Connection.handlerConnection.sendMessage(m);
                                    break;
                                case 1:
                                    MainActivity.MSG = "无法登录。可能的原因：\n" +
                                            "1.账户由于欠费被封锁；2.账户名错误。3.账户已注销。\n";
                                    m = new Message();
                                    m.what = 0;
                                    fragment_Connection.handlerConnection.sendMessage(m);
                                    break;
                                case 2:
                                    MainActivity.MSG = MainActivity.context.getString(R.string.TooManyConnections);
                                    m = new Message();
                                    m.what = 0;
                                    fragment_Connection.handlerConnection.sendMessage(m);
                                    break;
                                case 3:
                                    MainActivity.MSG = MainActivity.context.getString(R.string.AccountPasswordCantBeNull);
                                    m = new Message();
                                    m.what = 0;
                                    fragment_Connection.handlerConnection.sendMessage(m);
                                    break;
                                case 4:
                                    MainActivity.MSG = MainActivity.context.getString(R.string.SuccessfullyConnected);
                                    m = new Message();
                                    m.what = 2;
                                    utilsHandler.sendMessage(m);
                                    break;
                                case 5:
                                case 6:
                                    NetworkAction(username, password, "connect");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    exist = false;
                } catch (Exception e) {
                }
            }
        });
        thread_overtime = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(MainActivity.getOvertime());
                    if (exist==false)
                        return;
                    MainActivity.MSG = MainActivity.context.getString(R.string.ConnectionOvertime);
                    Message m = new Message();
                    m.what = 1;
                    utilsHandler.obtainMessage();
                    utilsHandler.sendMessage(m);
                } catch (Exception e) {
                } /*
                if (exist) {
                    thread_connect.interrupt();
                    MainActivity.MSG = MainActivity.context.getString(R.string.Overtime);
                    //在这里往主线程发送消息
                    Message m = fragment_Connection.handlerConnection.obtainMessage();
                    m.what = 0;
                    fragment_Connection.handlerConnection.sendMessage(m);
                    exist = false;
                } else {
                    exist = false;
                }*/
            }
        });
        thread_connect.start();
        thread_overtime.start();
    }

    /**
     * 无视安全证书
     * Created by xjh96 on 2015/7/26
     */
    private static void initTrustSSL() {
        try {
            SSLContext sslCtx = SSLContext.getInstance("TLS");
            sslCtx.init(null, new TrustManager[]{new X509TrustManager() {
                // do nothing, let the check pass.
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     *
     * @param view 当前视图view
     * @param context 当前context
     * @param theme 主题名称
     * @param imageView 图片view
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void applyTheme(View view, Context context, String theme, ImageView imageView){
        switch (theme){
            case "Pink":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Pink));
                imageView.setImageResource(R.drawable.pink);
                break;
            case "Blue":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Blue));
                imageView.setImageResource(R.drawable.blue);
                break;
            case "Yellow":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Yellow));
                imageView.setImageResource(R.drawable.yellow);
                break;
        }
    }

    /**
     *
     * @param view 当前视图
     * @param context 当前context
     * @param theme 主题名称
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void applyTheme(View view, Context context, String theme, View[] diviers){
        switch (theme){
            case "Pink":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Pink));
                for (View divier: diviers){
                    divier.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Pink));
                }
                break;
            case "Blue":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Blue));
                for (View divier: diviers){
                    divier.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Blue));
                }
                break;
            case "Yellow":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Yellow));
                for (View divier: diviers){
                    divier.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Yellow));
                }
                break;
            default:
                break;
        }
    }
    /**
     *
     * @param view 当前视图
     * @param context 当前context
     * @param theme 主题名称
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void applyTheme(View view, Context context, String theme){
        switch (theme){
            case "Pink":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Pink));
            case "Blue":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Blue));
                break;
            case "Yellow":
                view.setBackgroundColor(context.getResources().getColor(R.color.colorBackground_Yellow));
                break;
            default:
                break;
        }
    }

}
/*public class TimeoutException extends RuntimeException{
        *//**
 * 序列化号
 * <p/>
 * 计时器时间
 * <p/>
 * 计时器是否被取消
 * <p/>
 * 当计时器取消时抛出的异常
 * <p/>
 * 构造器
 *
 * @param overtime 指定的超时时间
 * <p/>
 * 取消计时
 * <p/>
 * 启动计时器
 * <p/>
 * 计时器时间
 * <p/>
 * 计时器是否被取消
 * <p/>
 * 当计时器取消时抛出的异常
 * <p/>
 * 构造器
 * @param overtime 指定的超时时间
 * <p/>
 * 取消计时
 * <p/>
 * 启动计时器
 *//*
        private static final long serialVersionUID = -8078853655388692688L;
        public TimeoutException(String errMessage){
            super(errMessage);
        }
    }
    public class TimeoutThread extends Thread{
        *//**
 * 计时器时间
 *//*
        private int timeout;
        *//**
 * 计时器是否被取消
 *//*
        private boolean isCanceled = false;
        *//**
 * 当计时器取消时抛出的异常
 *//*
        private TimeoutException timeoutException;
        *//**
 * 构造器
 * @param overtime 指定的超时时间
 *//*
        public TimeoutThread(int overtime, TimeoutException timeoutErr){
            super();
            this.timeout = overtime;
            this.timeoutException = timeoutErr;

            //设置守护进程
            this.setDaemon(true);
        }
        *//**
 * 取消计时
 *//*
        public synchronized void cancel(){
            isCanceled = true;
        }
        *//**
 * 启动计时器
 *//*
        public void run(){
            try{
                Thread.sleep(timeout);
                if (!isCanceled)
                    throw timeoutException;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
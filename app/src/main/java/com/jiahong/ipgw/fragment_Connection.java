package com.jiahong.ipgw;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.dd.CircularProgressButton;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.jar.Manifest;

/**
 * Created by xjh96 on 2015/10/14.
 */
public class fragment_Connection extends Fragment {
    private View view;
    public static Handler handlerConnection;
    private EditText et_Account;
    private EditText et_Password;
    private CheckBox cb_RemmemberAccount;
    private CheckBox cb_RemmemberPassword;
    private ImageView iv_Theme;
    private CircularProgressButton circularProgressButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection, container, false);
        init();
        return view;
    }

    private void init() {
        final CircularProgressButton btConnect = (CircularProgressButton) view.findViewById(R.id.btCircleProgressBar);
        circularProgressButton = btConnect;
        iv_Theme = (ImageView) view.findViewById(R.id.iv_pic);
        et_Account = (EditText) view.findViewById(R.id.et_Account);
        et_Password = (EditText) view.findViewById(R.id.et_Password);
        cb_RemmemberAccount = (CheckBox) view.findViewById(R.id.cb_rememberAccount);
        cb_RemmemberPassword = (CheckBox) view.findViewById(R.id.cb_rememberPassword);

        cb_RemmemberAccount.setChecked(MainActivity.getIsRemmemberAccount());
        cb_RemmemberPassword.setChecked(MainActivity.getIsRemmemberPassword());
        et_Account.setText(MainActivity.getAccount());
        et_Password.setText(MainActivity.getPassword());

        Utils.applyTheme(view, MainActivity.context, MainActivity.theme, iv_Theme);
        btConnect.setIndeterminateProgressMode(true);
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btConnect.getProgress() == 0) {
                    if (et_Account.getText().toString().trim().equals("")||et_Password.getText().toString().trim().equals("")){
                        final Snackbar snackbar = Snackbar.make(view, MainActivity.context.getString(R.string.AccountPasswordCantBeNull), Snackbar.LENGTH_LONG);
                        snackbar.setAction(MainActivity.context.getString(R.string.OK), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }else {
                        enableViews(false);
                        btConnect.setProgress(50);
                        new Utils().NetworkAction(et_Account.getText().toString(), et_Password.getText().toString(), "disconnectall");
                    }
                } else if (btConnect.getProgress() == 100) {
                    btConnect.setProgress(0);
                } else {/*
                    enableViews(true);
                    //取消连接，恢复未连接状态
                    btConnect.setProgress(0);*/
                    final Snackbar snackbar = Snackbar.make(view, MainActivity.context.getString(R.string.ConnectingPleaseWait),Snackbar.LENGTH_SHORT);
                    snackbar.setAction(MainActivity.context.getString(R.string.CancelConnection), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Message m = new Message();
                            m.what = 0;
                            Utils.utilsHandler.obtainMessage();
                            Utils.utilsHandler.sendMessage(m);
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });

        cb_RemmemberAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.setIsRemmemberAccount(isChecked);
                if (isChecked) {
                    MainActivity.setAccount(et_Account.getText().toString());
                }else cb_RemmemberPassword.setChecked(false);
            }
        });

        cb_RemmemberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.setIsRemmemberPassword(isChecked);
                if (isChecked) {
                    MainActivity.setPassword(et_Password.getText().toString());
                    cb_RemmemberAccount.setChecked(true);
                }
            }
        });

        et_Account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.getIsRemmemberAccount()) {
                    MainActivity.setAccount(et_Account.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Account.getText().toString().trim().equals("")){
                    cb_RemmemberAccount.setChecked(false);
                }else cb_RemmemberAccount.setEnabled(true);
            }
        });

        et_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (MainActivity.getIsRemmemberPassword()) {
                    MainActivity.setPassword(et_Password.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Password.getText().toString().trim().equals("")) {
                    cb_RemmemberPassword.setChecked(false);
                }else cb_RemmemberPassword.setEnabled(true);
            }
        });

        handlerConnection = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0://接收连接失败
                        btConnect.setProgress(-1);
                        final Snackbar snackbar = Snackbar.make(view, MainActivity.MSG, Snackbar.LENGTH_LONG);
                        snackbar.setAction(MainActivity.context.getString(R.string.OK), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                    Message m = new Message();
                                    m.what = 2;
                                    handlerConnection.obtainMessage();
                                    handlerConnection.sendMessage(m);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        break;
                    case 1://接收连接成功
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    Message m = new Message();
                                    m.what = 3;
                                    handlerConnection.obtainMessage();
                                    handlerConnection.sendMessage(m);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        break;
                    case 2://恢复
                        enableViews(true);
                        btConnect.setProgress(0);
                        break;
                    case 3://显示连接成功
                        enableViews(true);
                        btConnect.setProgress(100);
                        MainActivity.referWeather();
                        break;
                    default:
                        break;
                }
            }
        };

        /*if (isNetworkConnected(MainActivity.context)){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.context);
            mBuilder.setContentTitle("钰鑫好漂亮~")
                    .setContentText("有网络连接")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.ic_launcher);


            Notification notification = mBuilder.build();
            MainActivity.notificationManager.notify(1, notification);
            MainActivity.notificationManager.cancel(520);
        }else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.context);
            mBuilder.setContentTitle("钰鑫好漂亮~")
                    .setContentText("没有网络连接")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.ic_launcher);


            Notification notification = mBuilder.build();
            MainActivity.notificationManager.notify(1, notification);
            MainActivity.notificationManager.cancel(520);
        }*/
    }

    private void enableViews(boolean enabled){
        et_Account.setEnabled(enabled);
        et_Password.setEnabled(enabled);
        cb_RemmemberAccount.setEnabled(enabled);
        cb_RemmemberPassword.setEnabled(enabled);
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
}

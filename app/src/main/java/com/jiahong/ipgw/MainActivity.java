package com.jiahong.ipgw;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    public static Context context;
    private static SharedPreferences userdata;
    private static SharedPreferences.Editor editor;
    private static long OVERTIME;
    public static String MSG = "^_^";
    public static String theme;
    private static boolean isRemmemberAccount;
    private static boolean isRemmemberPassword;
    public static NotificationManager notificationManager;
    private static fragment_Connection fragment_connection;
    private static fragment_Weather fragment_weather;
    private static fragment_Calender fragment_calender;
    public static String datongWeather;
    public static String shenyangWeather;
    public static Weather datong;
    public static Weather shenyang;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_connection = new fragment_Connection();
        fragment_weather = new fragment_Weather();
        fragment_calender = new fragment_Calender();

        Intent i = new Intent(MainActivity.this, ScanWifiService.class);
/*        i.setAction("com.jiahong.ipgw.ScanWifi");
        i.setPackage(getPackageName());*/
        startService(i);

        userdata = getSharedPreferences("userdata", MODE_PRIVATE);
        theme = userdata.getString("Theme", "Pink");
        switch (theme) {
            case "Pink":
                setTheme(R.style.AppTheme_Pink);
                break;
            case "Blue":
                setTheme(R.style.AppTheme_Blue);
                break;
            case "Yellow":
                setTheme(R.style.AppTheme_Yellow);
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            switch (theme) {
                case "Pink":
                    tintManager.setStatusBarTintResource(R.color.colorBackground_Pink);//通知栏所需颜色
//                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Pink);//通知栏所需颜色
                    break;
                case "Blue":
//                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Blue);//通知栏所需颜色
                    tintManager.setStatusBarTintResource(R.color.colorBackground_Blue);//通知栏所需颜色
                    break;
                case "Yellow":
                    tintManager.setStatusBarTintResource(R.color.colorBackground_Yellow);//通知栏所需颜色
//                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Yellow);//通知栏所需颜色
                    break;
            }
        }

        datongWeather = userdata.getString("datongWeather", "{\"desc\":\"OK\",\"status\":1000,\"data\":{\"wendu\":\"0\",\"ganmao\":\"天气未获取嘻嘻嘻\",\"forecast\":[{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"嘻嘻\",\"low\":\"低温\",\"date\":\"我\"},{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"嘻嘻\",\"low\":\"低温\",\"date\":\"爱\"},{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"\",\"low\":\"低温\",\"date\":\"你\"},{\"fengxiang\":\"西北风\",\"fengli\":\"微风级\",\"high\":\"高温 1℃\",\"type\":\"晴\",\"low\":\"低温 -10℃\",\"date\":\"16日星期二\"},{\"fengxiang\":\"西南风\",\"fengli\":\"微风级\",\"high\":\"高温 4℃\",\"type\":\"晴\",\"low\":\"低温 -9℃\",\"date\":\"17日星期三\"}],\"yesterday\":{\"fl\":\"3-4级\",\"fx\":\"北风\",\"high\":\"高温 7℃\",\"type\":\"阵雨\",\"low\":\"低温 -5℃\",\"date\":\"12日星期五\"},\"aqi\":\"61\",\"city\":\"沈阳\"}}");
        shenyangWeather = userdata.getString("shenyangWeather", "{\"desc\":\"OK\",\"status\":1000,\"data\":{\"wendu\":\"0\",\"ganmao\":\"天气未获取嘻嘻嘻\",\"forecast\":[{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"嘻嘻\",\"low\":\"低温\",\"date\":\"我\"},{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"嘻嘻\",\"low\":\"低温\",\"date\":\"爱\"},{\"fengxiang\":\"风向\",\"fengli\":\"风力\",\"high\":\"高温\",\"type\":\"\",\"low\":\"低温\",\"date\":\"你\"},{\"fengxiang\":\"西北风\",\"fengli\":\"微风级\",\"high\":\"高温 1℃\",\"type\":\"晴\",\"low\":\"低温 -10℃\",\"date\":\"16日星期二\"},{\"fengxiang\":\"西南风\",\"fengli\":\"微风级\",\"high\":\"高温 4℃\",\"type\":\"晴\",\"low\":\"低温 -9℃\",\"date\":\"17日星期三\"}],\"yesterday\":{\"fl\":\"3-4级\",\"fx\":\"北风\",\"high\":\"高温 7℃\",\"type\":\"阵雨\",\"low\":\"低温 -5℃\",\"date\":\"12日星期五\"},\"aqi\":\"61\",\"city\":\"沈阳\"}}");

        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        //初始化数据
        initData();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Setting.class);
                Bundle bundle = new Bundle();
                bundle.putString("Theme", theme);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("钰鑫好漂亮~")
                .setContentText("钰鑫钰鑫开开心心！爱你~♥")
                .setContentIntent(getDefaultIntent(Notification.FLAG_NO_CLEAR))
                .setNumber(520)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setOngoing(true)
//                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.drawable.icon)
                .setPriority(Notification.PRIORITY_HIGH);


        Notification notification = mBuilder.build();
        notificationManager.notify(520, notification);

            referWeather();

    }

    private PendingIntent getDefaultIntent(int flags) {
        Intent intent/* = new Intent(Intent.ACTION_MAIN)*/;
        intent = new Intent(getApplicationContext(), MainActivity.class);/*
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(getPackageName(),".MainActivity"));*/
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, flags);
        return pendingIntent;
    }

    public static void referWeather() {
        final String[] temp = {"", ""};
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    temp[0] = sendGet("http://wthrcdn.etouch.cn/weather_mini?city=%E5%A4%A7%E5%90%8C");
                    temp[1] = sendGet("http://wthrcdn.etouch.cn/weather_mini?city=%E6%B2%88%E9%98%B3");
                    JSONObject jsonObject = new JSONObject(temp[0]);
                    if (jsonObject.getString("desc").equals("OK")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        System.out.println(data.getString("wendu"));

                        datong = new Weather(data);
                    }

                    jsonObject = new JSONObject(temp[1]);
                    if (jsonObject.getString("desc").equals("OK")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        System.out.println(data.getString("wendu"));

                        shenyang = new Weather(data);
                    }

//                    System.out.println(shenyang.getTempNow());

                    userdata.edit().putString("datongWeather", temp[0]).commit();
                    userdata.edit().putString("shenyangWeather", temp[1]).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(datongWeather);
                        JSONObject data = jsonObject.getJSONObject("data");
                        System.out.println(data.getString("wendu"));

                        datong = new Weather(data);

                        jsonObject = new JSONObject(shenyangWeather);
                        data = jsonObject.getJSONObject("data");
                        System.out.println(data.getString("wendu"));

                        shenyang = new Weather(data);

                        Message m = new Message();
                        m.what = 0;
                        fragment_Weather.handler.obtainMessage();
                        fragment_Weather.handler.sendMessage(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = fragment_connection;
                    break;
                case 1:
                    fragment = fragment_weather;
                    break;
                case 2:
                    fragment = fragment_calender;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private void initData() {
        editor = userdata.edit();
        OVERTIME = userdata.getLong("overtime", 5000);
        isRemmemberAccount = userdata.getBoolean("isRemmemberAccount", false);
        isRemmemberPassword = userdata.getBoolean("isRemmemberPassword", false);
    }

    public static void setOverTime(long overtime) {
        OVERTIME = overtime;
        editor.putLong("overtime", OVERTIME);
        editor.commit();
    }

    public static long getOvertime() {
        return OVERTIME;
    }

    public static void setIsRemmemberAccount(boolean checked) {
        isRemmemberAccount = checked;
        if (!checked)
            editor.putString("Account", "");
        editor.putBoolean("isRemmemberAccount", checked);
        editor.commit();
    }

    public static boolean getIsRemmemberAccount() {
        return isRemmemberAccount;
    }

    public static void setIsRemmemberPassword(boolean checked) {
        isRemmemberPassword = checked;
        if (!checked)
            editor.putString("Password", "");
        editor.putBoolean("isRemmemberPassword", checked);
        editor.commit();
    }

    public static boolean getIsRemmemberPassword() {
        return isRemmemberPassword;
    }

    public static void setAccount(String account) {
        editor.putString("Account", account);
        editor.commit();
    }

    public static String getAccount() {
        return userdata.getString("Account", "");
    }

    public static void setPassword(String password) {
        editor.putString("Password", password);
        editor.commit();
    }

    public static void setCustomTheme(String theme) {
        editor.putString("Theme", theme);
        editor.commit();
    }

    public static String getCustomTheme() {
        return userdata.getString("Theme", "Pink");
    }

    public static String getPassword() {
        return userdata.getString("Password", "");
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static String sendGet(String url) {
        String result = "";
        URL readURL = null;
        URLConnection conn = null;
        BufferedReader bf = null;
        String line = "";

        try {
            readURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            conn = readURL.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bf = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));

            while ((line = bf.readLine()) != null) {
                result += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
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

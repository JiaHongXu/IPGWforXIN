package com.jiahong.ipgw;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Setting extends AppCompatActivity {
    private FloatingActionButton fab;
    private SeekBar sb_overtime;
    private Context context;
    private Switch sw_autoConnect;
    private TextView tv_overtime;
    private CardView cv_theme;
    private View[] dividers;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = getIntent().getExtras().getString("Theme", "Pink");
        switch (theme){
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
                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Pink);//通知栏所需颜色
                    break;
                case "Blue":
                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Blue);//通知栏所需颜色
                    break;
                case "Yellow":
                    tintManager.setStatusBarTintResource(R.color.colorPrimaryDark_Yellow);//通知栏所需颜色
                    break;
            }
        }
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     fab = (FloatingActionButton) findViewById(R.id.fab);
        sb_overtime = (SeekBar) findViewById(R.id.sb_overtime);
        sw_autoConnect = (Switch) findViewById(R.id.sw_autoConnect);
        tv_overtime = (TextView) findViewById(R.id.tv_overtime);
        cv_theme = (CardView) findViewById(R.id.cv_theme);
        dividers = new View[]{findViewById(R.id.divider_0)};
        init();
    }

    private void init(){
        context = Setting.this;
        Utils.applyTheme(getWindow().getDecorView(),context, MainActivity.theme, dividers);
        DecimalFormat decimalFormat = new DecimalFormat(".#");
        final String[] themes = {getString(R.string.HelloKitty),getString(R.string.Doraemon),getString(R.string.SpongeBob)};
        cv_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.selectedID = -1;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.ChooseTheme);
                builder.setSingleChoiceItems(themes, themes.length, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.selectedID = which;
                    }
                })
                        .setPositiveButton(getString(R.string.This), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (Utils.selectedID) {
                                    case 0:
                                        MainActivity.setCustomTheme("Pink");
                                        break;
                                    case 1:
                                        MainActivity.setCustomTheme("Blue");
                                        break;
                                    case 2:
                                        MainActivity.setCustomTheme("Yellow");
                                        break;
                                    default:
                                        break;
                                }
                                switch (Utils.selectedID) {
                                    case -1: {
                                        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "臣妾办不到啊T^T", Snackbar.LENGTH_SHORT);
                                        snackbar.setAction(MainActivity.context.getString(R.string.OK), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                snackbar.dismiss();
                                            }
                                        });
                                        snackbar.show();
                                        break;
                                    }
                                    default: {
                                                Intent i = getBaseContext().getPackageManager()
                                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                        break;
                                    }
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.Cancel), null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "分享到社交网络", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tv_overtime.setText(MainActivity.context.getString(R.string.Timeout) + ":" + String.valueOf(Float.parseFloat(decimalFormat.format((float)MainActivity.getOvertime() / 1000))) + "s");
        sb_overtime.setProgress((int) (MainActivity.getOvertime() - 4000));
        sb_overtime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            long sec;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DecimalFormat decimalFormat = new DecimalFormat(".#");
                sec = progress + 4000;
                tv_overtime.setText(MainActivity.context.getString(R.string.Timeout) + ":" + String.valueOf(Float.parseFloat(decimalFormat.format((float) (progress) / 1000 + 4.0))) + "s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //记录当前值
                MainActivity.setOverTime(sec);
            }
        });
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
}

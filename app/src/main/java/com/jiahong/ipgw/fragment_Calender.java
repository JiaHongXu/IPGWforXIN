package com.jiahong.ipgw;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.CircularProgressButton;

/**
 * Created by xjh96 on 2015/10/14.
 */
public class fragment_Calender extends Fragment {
    private View view;
    public static Handler handlerConnection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        return view;
    }
}

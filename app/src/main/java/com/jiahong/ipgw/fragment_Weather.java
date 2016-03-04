package com.jiahong.ipgw;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xjh96 on 2016/2/13.
 */
public class fragment_Weather extends Fragment {

    public static View view;
    private View[] dividers;

    public static Weather datongWeather;
    public static Weather shenyangWeather;
    
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    loadData(view);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);

        dividers = new View[]{view.findViewById(R.id.divider_0)
                ,view.findViewById(R.id.divider_1)
                ,view.findViewById(R.id.divider_2)
                ,view.findViewById(R.id.divider_3)};

        Utils.applyTheme(view, MainActivity.context, MainActivity.theme, dividers);
        return view;
    }
    private static void loadData(View view){
        datongWeather = MainActivity.datong;
        shenyangWeather = MainActivity.shenyang;
        TextView shenyang_temp_textview;
        TextView shenyang_weather_textview;
        TextView shenyang_ganmao_textview;

        TextView shenyang_today_temp_high_textview;
        TextView shenyang_today_temp_low_textview;
        TextView shenyang_today_detial_textview;
        TextView shenyang_today_weather_textview;
        TextView shenyang_today_fengli_value_textview;

        TextView shenyang_tomorrow_temp_high_textview;
        TextView shenyang_tomorrow_temp_low_textview;
        TextView shenyang_tomorrow_detial_textview;
        TextView shenyang_tomorrow_weather_textview;
        TextView shenyang_tomorrow_fengli_value_textview;

        TextView shenyang_afterTomorrow_temp_high_textview;
        TextView shenyang_afterTomorrow_temp_low_textview;
        TextView shenyang_afterTomorrow_detial_textview;
        TextView shenyang_afterTomorrow_weather_textview;
        TextView shenyang_afterTomorrow_fengli_value_textview;


        shenyang_temp_textview = (TextView) view.findViewById(R.id.shenyang_temp_textview);
        shenyang_temp_textview.setText(shenyangWeather.getTempNow());
        shenyang_weather_textview = (TextView) view.findViewById(R.id.shenyang_weather_textview);
        shenyang_weather_textview.setText(shenyangWeather.getDatas().get(0).getType());
        shenyang_ganmao_textview = (TextView) view.findViewById(R.id.shenyang_ganmao_textview);
        shenyang_ganmao_textview.setText(shenyangWeather.getGanmao());

        shenyang_today_temp_high_textview = (TextView) view.findViewById(R.id.shenyang_today_temp_high_textview);
        shenyang_today_temp_low_textview = (TextView) view.findViewById(R.id.shenyang_today_temp_low_textview);
        shenyang_today_detial_textview = (TextView) view.findViewById(R.id.shenyang_today_detial_textview);
        shenyang_today_weather_textview = (TextView) view.findViewById(R.id.shenyang_today_weather_textview);
        shenyang_today_fengli_value_textview = (TextView) view.findViewById(R.id.shenyang_today_fengli_value_textview);
        shenyang_today_temp_high_textview.setText(shenyangWeather.getDatas().get(0).getHigh()); 
        shenyang_today_temp_low_textview.setText(shenyangWeather.getDatas().get(0).getLow());
        shenyang_today_detial_textview.setText(shenyangWeather.getDatas().get(0).getDate());
        shenyang_today_weather_textview.setText(shenyangWeather.getDatas().get(0).getType());
        shenyang_today_fengli_value_textview.setText(shenyangWeather.getDatas().get(0).getFengli());

        shenyang_tomorrow_temp_high_textview = (TextView) view.findViewById(R.id.shenyang_tomorrow_temp_high_textview);
        shenyang_tomorrow_temp_low_textview = (TextView) view.findViewById(R.id.shenyang_tomorrow_temp_low_textview);
        shenyang_tomorrow_detial_textview = (TextView) view.findViewById(R.id.shenyang_tomorrow_detial_textview);
        shenyang_tomorrow_weather_textview = (TextView) view.findViewById(R.id.shenyang_tomorrow_weather_textview);
        shenyang_tomorrow_fengli_value_textview = (TextView) view.findViewById(R.id.shenyang_tomorrow_fengli_value_textview);
        shenyang_tomorrow_temp_high_textview.setText(shenyangWeather.getDatas().get(1).getHigh());
        shenyang_tomorrow_temp_low_textview.setText(shenyangWeather.getDatas().get(1).getLow());
        shenyang_tomorrow_detial_textview.setText(shenyangWeather.getDatas().get(1).getDate());
        shenyang_tomorrow_weather_textview.setText(shenyangWeather.getDatas().get(1).getType());
        shenyang_tomorrow_fengli_value_textview.setText(shenyangWeather.getDatas().get(1).getFengli());


        shenyang_afterTomorrow_temp_high_textview = (TextView) view.findViewById(R.id.shenyang_afterTomorrow_temp_high_textview);
        shenyang_afterTomorrow_temp_low_textview = (TextView) view.findViewById(R.id.shenyang_afterTomorrow_temp_low_textview);
        shenyang_afterTomorrow_detial_textview = (TextView) view.findViewById(R.id.shenyang_afterTomorrow_detial_textview);
        shenyang_afterTomorrow_weather_textview = (TextView) view.findViewById(R.id.shenyang_afterTomorrow_weather_textview);
        shenyang_afterTomorrow_fengli_value_textview = (TextView) view.findViewById(R.id.shenyang_afterTomorrow_fengli_value_textview);
        shenyang_afterTomorrow_temp_high_textview.setText(shenyangWeather.getDatas().get(2).getHigh());
        shenyang_afterTomorrow_temp_low_textview.setText(shenyangWeather.getDatas().get(2).getLow());
        shenyang_afterTomorrow_detial_textview.setText(shenyangWeather.getDatas().get(2).getDate());
        shenyang_afterTomorrow_weather_textview.setText(shenyangWeather.getDatas().get(2).getType());
        shenyang_afterTomorrow_fengli_value_textview.setText(shenyangWeather.getDatas().get(2).getFengli());


        TextView datong_temp_textview;
        TextView datong_weather_textview;
        TextView datong_ganmao_textview;

        TextView datong_today_temp_high_textview;
        TextView datong_today_temp_low_textview;
        TextView datong_today_detial_textview;
        TextView datong_today_weather_textview;
        TextView datong_today_fengli_value_textview;

        TextView datong_tomorrow_temp_high_textview;
        TextView datong_tomorrow_temp_low_textview;
        TextView datong_tomorrow_detial_textview;
        TextView datong_tomorrow_weather_textview;
        TextView datong_tomorrow_fengli_value_textview;

        TextView datong_afterTomorrow_temp_high_textview;
        TextView datong_afterTomorrow_temp_low_textview;
        TextView datong_afterTomorrow_detial_textview;
        TextView datong_afterTomorrow_weather_textview;
        TextView datong_afterTomorrow_fengli_value_textview;


        datong_temp_textview = (TextView) view.findViewById(R.id.datong_temp_textview);
        datong_temp_textview.setText(datongWeather.getTempNow());
        datong_weather_textview = (TextView) view.findViewById(R.id.datong_weather_textview);
        datong_weather_textview.setText(datongWeather.getDatas().get(0).getType());
        datong_ganmao_textview = (TextView) view.findViewById(R.id.datong_ganmao_textview);
        datong_ganmao_textview.setText(datongWeather.getGanmao());

        datong_today_temp_high_textview = (TextView) view.findViewById(R.id.datong_today_temp_high_textview);
        datong_today_temp_low_textview = (TextView) view.findViewById(R.id.datong_today_temp_low_textview);
        datong_today_detial_textview = (TextView) view.findViewById(R.id.datong_today_detial_textview);
        datong_today_weather_textview = (TextView) view.findViewById(R.id.datong_today_weather_textview);
        datong_today_fengli_value_textview = (TextView) view.findViewById(R.id.datong_today_fengli_value_textview);
        datong_today_temp_high_textview.setText(datongWeather.getDatas().get(0).getHigh());
        datong_today_temp_low_textview.setText(datongWeather.getDatas().get(0).getLow());
        datong_today_detial_textview.setText(datongWeather.getDatas().get(0).getDate());
        datong_today_weather_textview.setText(datongWeather.getDatas().get(0).getType());
        datong_today_fengli_value_textview.setText(datongWeather.getDatas().get(0).getFengli());


        datong_tomorrow_temp_high_textview = (TextView) view.findViewById(R.id.datong_tomorrow_temp_high_textview);
        datong_tomorrow_temp_low_textview = (TextView) view.findViewById(R.id.datong_tomorrow_temp_low_textview);
        datong_tomorrow_detial_textview = (TextView) view.findViewById(R.id.datong_tomorrow_detial_textview);
        datong_tomorrow_weather_textview = (TextView) view.findViewById(R.id.datong_tomorrow_weather_textview);
        datong_tomorrow_fengli_value_textview = (TextView) view.findViewById(R.id.datong_tomorrow_fengli_value_textview);
        datong_tomorrow_temp_high_textview.setText(datongWeather.getDatas().get(1).getHigh());
        datong_tomorrow_temp_low_textview.setText(datongWeather.getDatas().get(1).getLow());
        datong_tomorrow_detial_textview.setText(datongWeather.getDatas().get(1).getDate());
        datong_tomorrow_weather_textview.setText(datongWeather.getDatas().get(1).getType());
        datong_tomorrow_fengli_value_textview.setText(datongWeather.getDatas().get(1).getFengli());


        datong_afterTomorrow_temp_high_textview = (TextView) view.findViewById(R.id.datong_afterTomorrow_temp_high_textview);
        datong_afterTomorrow_temp_low_textview = (TextView) view.findViewById(R.id.datong_afterTomorrow_temp_low_textview);
        datong_afterTomorrow_detial_textview = (TextView) view.findViewById(R.id.datong_afterTomorrow_detial_textview);
        datong_afterTomorrow_weather_textview = (TextView) view.findViewById(R.id.datong_afterTomorrow_weather_textview);
        datong_afterTomorrow_fengli_value_textview = (TextView) view.findViewById(R.id.datong_afterTomorrow_fengli_value_textview);
        datong_afterTomorrow_temp_high_textview.setText(datongWeather.getDatas().get(2).getHigh());
        datong_afterTomorrow_temp_low_textview.setText(datongWeather.getDatas().get(2).getLow());
        datong_afterTomorrow_detial_textview.setText(datongWeather.getDatas().get(2).getDate());
        datong_afterTomorrow_weather_textview.setText(datongWeather.getDatas().get(2).getType());
        datong_afterTomorrow_fengli_value_textview.setText(datongWeather.getDatas().get(2).getFengli());
    }
}

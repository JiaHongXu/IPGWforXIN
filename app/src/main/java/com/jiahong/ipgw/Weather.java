package com.jiahong.ipgw;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xjh96 on 2016/2/13.
 */
public class Weather {
    private String tempNow;
    private String ganmao;
    private ArrayList<Data> datas;

    Weather(JSONObject jsonObject){
        try {
            this.tempNow = jsonObject.getString("wendu");
            this.ganmao = jsonObject.getString("ganmao");
            JSONArray jsonArray = jsonObject.getJSONArray("forecast");
            datas = new ArrayList();
            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    datas.add(new Data(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            System.out.println(tempNow + " " + ganmao);
        }

    }

    public String getTempNow() {
        return tempNow;
    }

    public String getGanmao() {
        return ganmao;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public class Data{
        private String fengli;
        private String high;
        private String low;
        private String type;
        private String date;

        public Data(JSONObject jsonObject) {
            try {
                fengli = jsonObject.getString("fengli");
                high = jsonObject.getString("high");
                high = high.replace("高温 ","");
                high.trim();
                low = jsonObject.getString("low");
                low = low.replace("低温","");
                low.trim();
                type = jsonObject.getString("type");
                date = jsonObject.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getFengli() {
            return fengli;
        }

        public String getHigh() {
            return high;
        }

        public String getLow() {
            return low;
        }

        public String getType() {
            return type;
        }

        public String getDate() {
            return date;
        }
    }
}

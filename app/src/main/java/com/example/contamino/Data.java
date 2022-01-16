package com.example.contamino;

public class Data {
    String aqi_frag,pm10_frag,co_frag,o3_frag,so2_frag,last_checked,no2_frag,city_json;

    public Data(String aqi_frag, String pm10_frag, String co_frag, String o3_frag, String so2_frag, String last_checked,String no2_frag, String city_json) {
        this.aqi_frag = aqi_frag;
        this.pm10_frag = pm10_frag;
        this.co_frag = co_frag;
        this.o3_frag = o3_frag;
        this.so2_frag = so2_frag;
        this.last_checked = last_checked;
        this.no2_frag = no2_frag;
        this.city_json= city_json;
    }

    public String getNo2_frag() {
        return no2_frag;
    }

    public void setNo2_frag(String no2_frag) {
        this.no2_frag = no2_frag;
    }

    public String getCity_json() {
        return city_json;
    }

    public void setCity_json(String city_json) {
        this.city_json = city_json;
    }

    public String getAqi_frag() {
        return aqi_frag;
    }

    public void setAqi_frag(String aqi_frag) {
        this.aqi_frag = aqi_frag;
    }

    public String getPm10_frag() {
        return pm10_frag;
    }

    public void setPm10_frag(String pm10_frag) {
        this.pm10_frag = pm10_frag;
    }

    public String getCo_frag() {
        return co_frag;
    }

    public void setCo_frag(String co_frag) {
        this.co_frag = co_frag;
    }

    public String getO3_frag() {
        return o3_frag;
    }

    public void setO3_frag(String o3_frag) {
        this.o3_frag = o3_frag;
    }

    public String getSo2_frag() {
        return so2_frag;
    }

    public void setSo2_frag(String so2_frag) {
        this.so2_frag = so2_frag;
    }

    public String getLast_checked() {
        return last_checked;
    }

    public void setLast_checked(String last_checked) {
        this.last_checked = last_checked;
    }
}

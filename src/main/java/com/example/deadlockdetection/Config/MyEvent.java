package com.example.deadlockdetection.Config;
import net.sf.json.JSONObject;
public class MyEvent {
    private String type;
    private JSONObject data;

    public MyEvent(String type, Object data) {
        this.type = type;
        this.data = (JSONObject) data;
    }

    public MyEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public JSONObject getData() {
        return data;
    }
}

package com.example.smarthome;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Communication {
    private static final String TAG = "Communication";
    private static String ip;
    private static String port;

    public static OkHttpClient client;

    public static OkHttpClient getClient(){
        if(client == null){
            client = new OkHttpClient();
        }
        return client;
    }

    private static void getJsonStringResponse(String url, Callback callback){
        client = getClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean connection(final String ip, final String port){
        Communication.ip = ip;
        Communication.port = port;
        String url = "http://" + ip + ":" + port + "/connection";
        final boolean[] connectionStatus = {false};
        getJsonStringResponse(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"Connection failed", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject conn = new JSONObject(result);
                        connectionStatus[0] = conn.getBoolean("connection_status");
                    }catch (JSONException joe){
                        Log.e(TAG, "Ошибка обработки JSON", joe);
                    }
                }
            }
        });
        return connectionStatus[0];
    }

    public static List<Controller> getList(){
        String url = "http://" + Communication.ip + ":" + Communication.port + "/installation-scheme";

        List<Controller> controllers = new ArrayList<>();

        getJsonStringResponse(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"Connection failed", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString = response.body().string();
                try {
                    JSONObject jsonBody = new JSONObject(jsonString);
                    parseItems(controllers, jsonBody);
                }catch (JSONException joe){
                    Log.e(TAG, "Ошибка обработки JSON", joe);
                }
            }
        });
        return controllers;
    }

    private static void parseItems(List<Controller> items, JSONObject jsonBody) throws
            IOException, JSONException {
        JSONArray controllersArray = jsonBody.getJSONArray("schemes");

        for (int i = 0; i < controllersArray.length(); i++) {
            JSONObject controllerJsonObject = controllersArray.getJSONObject(i);
            Controller item = new Controller();
            item.setId(controllerJsonObject.getInt("id"));
            item.setDisplayable_name(controllerJsonObject.getString("displayable_name"));
            item.setRoom_name(controllerJsonObject.getString("room_name"));
            item.setEquipment_name(controllerJsonObject.getString("equipment_name"));
            item.setStatus(controllerJsonObject.getBoolean("status"));
            items.add(item);
        }
    }
}

package com.example.smarthome;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Communication {
    private static final String TAG = "Communication";
    private static String ip;
    private static String port;

    public static OkHttpClient client;

    public static OkHttpClient getClient(){
        if(client == null){
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.cookieJar(new CookieJar() {
                private List<Cookie> cookies;

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    this.cookies = cookies;
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    if(cookies != null)
                        return cookies;
                    return new ArrayList<Cookie>();
                }
            });
            client = clientBuilder.build();
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

    private static void postJsonStringResponse(String url, Callback callback, List<Params> params){
        client = getClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
                for(int i = 0; i < params.size(); i++){
                    Params param = params.get(i);
                    formBuilder.add(param.getKey(),param.getParam());
                }
        RequestBody formbody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formbody)
                .build();
        Log.i(TAG, String.valueOf(request));
        client.newCall(request).enqueue(callback);
    }

    public static void connection(final String ip, final String port, String login, String pass, Callback callback){
        Communication.ip = ip;
        Communication.port = port;
        Log.i(TAG,"connection address:"+ Communication.ip + ":" +Communication.port);
//        String url = "http://" + ip + ":" + port + "/connection";
        String url = "http://" + ip + ":" + port + "/login/mobile";
        Log.i(TAG,"connection url:" + url);

        List params = new ArrayList<Params>();
        params.add(new Params("login",login));
        params.add(new Params("password",pass));
        postJsonStringResponse(url, callback, params);

//        getJsonStringResponse(url, callback);

    }

    public static void getList(Callback callback){
        Log.i(TAG,"get list address:" + Communication.ip + ":" + Communication.port);
//        String url = "http://" + Communication.ip + ":" + Communication.port + "/installation-scheme";
        String url = "http://" + Communication.ip + ":" + Communication.port + "/installation-scheme/mobile";
        Log.i(TAG,"get list url:" + url);

        getJsonStringResponse(url,callback);
    }

    public static void changeStatus(Controller controller,Callback callback){
        Log.i(TAG,"change status address:" + Communication.ip + ":" + Communication.port);
        int id = controller.getId();
        String status = controller.getStatus() == "1" ? "1" : "0";
//        String url = "http://" + Communication.ip + ":" + Communication.port + "/installation-scheme/"+ id + "/" + status + "/change-status";
        String url = "http://" + Communication.ip + ":" + Communication.port + "/installation-scheme/"+ id + "/" + status + "/change-status/mobile";
        Log.i(TAG,"change status url:" + url);

        getJsonStringResponse(url,callback);
    }

    public static void parseItems(List<Controller> items, JSONObject jsonBody) throws
            IOException, JSONException {
        JSONArray controllersArray = jsonBody.getJSONArray("schemes");
        Log.i(TAG,"inside parser");
        for (int i = 0; i < controllersArray.length(); i++) {
            JSONObject controllerJsonObject = controllersArray.getJSONObject(i);
            Controller item = new Controller();
            item.setId(controllerJsonObject.getInt("id"));
            item.setDisplayable_name(controllerJsonObject.getString("displayable_name"));
            item.setRoom_name(controllerJsonObject.getString("room_name"));
            item.setEquipment_name(controllerJsonObject.getString("equipment_name"));
            item.setStatus(controllerJsonObject.getInt("status"));
            Log.i(TAG, "item-" + i);
            items.add(item);
        }
    }

    private static class Params {
        private String key;
        private String param;

        public Params(String key, String param) {
            this.key = key;
            this.param = param;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }
}

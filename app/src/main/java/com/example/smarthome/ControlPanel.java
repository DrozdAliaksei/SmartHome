package com.example.smarthome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ControlPanel extends AppCompatActivity {
    private static final String TAG = "ControlPanel";

    private RecyclerView controllersRecycleView;
    private List<Controller> controllerList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_panel);

        Communication.getList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"Connection failed", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String result = response.body().string();
                    Log.i(TAG,result);
                    try {
                        JSONObject jsonBody = new JSONObject(result);
                        Communication.parseItems(controllerList, jsonBody);

                        ControlPanel.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecycleView();
                            }
                        });

                    }catch (JSONException joe){
                        Log.e(TAG, "Ошибка обработки JSON", joe);
                    }
                }
            }
        });
    }

    private void initRecycleView(){
        controllersRecycleView = findViewById(R.id.recycle_view);
        ControllerAdapter adapter = new ControllerAdapter(this,controllerList);
        controllersRecycleView.setAdapter(adapter);
        controllersRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}

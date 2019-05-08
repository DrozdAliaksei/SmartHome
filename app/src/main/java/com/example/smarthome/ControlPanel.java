package com.example.smarthome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends AppCompatActivity {
    private static final String TAG = "ControlPanel";

    private RecyclerView controllersRecycleView;
    private List<Controller> controllerList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_panel);

        controllerList = Communication.getList();
        initRecycleView();
    }

    private void initRecycleView(){
        controllersRecycleView = findViewById(R.id.recycle_view);
        ControllerAdapter adapter = new ControllerAdapter(this,controllerList);
        controllersRecycleView.setAdapter(adapter);
        controllersRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }
}

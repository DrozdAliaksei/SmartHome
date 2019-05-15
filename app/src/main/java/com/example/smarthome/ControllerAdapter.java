package com.example.smarthome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ControllerAdapter extends RecyclerView.Adapter<ControllerAdapter.ViewHolder>{
    private static final String TAG = "ControllerAdapter";

    private List<Controller> mControllersItems = new ArrayList<>();
    private Context context;

    public ControllerAdapter(Context context, List<Controller> mItems) {
        this.mControllersItems = mItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.controller,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Controller item = mControllersItems.get(i);
        viewHolder.displayableName.setText(item.getDisplayable_name());
        viewHolder.roomName.setText(item.getRoom_name());
        viewHolder.equipmentName.setText(item.getEquipment_name());
        viewHolder.status.setChecked(item.isStatus());

        viewHolder.status.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG,item.getDisplayable_name() + " " + isChecked);
                Communication.changeStatus(item, new Callback() {
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
                                Log.i(TAG,"status before:" + item.getStatus());
                                JSONObject controller = jsonBody.getJSONObject("controller");
                                int status = controller.getInt("status");
                                Log.i(TAG,"status:" + status );
                                item.setStatus(status);
                                Log.i(TAG,"status after:" + item.getStatus());
//                                viewHolder.status.setChecked(item.isStatus());

                            }catch (JSONException joe){
                                Log.e(TAG, "Ошибка обработки JSON", joe);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mControllersItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayableName;
        TextView roomName;
        TextView equipmentName;
        Switch status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayableName = itemView.findViewById(R.id.displayable_name);
            roomName = itemView.findViewById(R.id.room_name);
            equipmentName = itemView.findViewById(R.id.equipment_name);
            status = itemView.findViewById(R.id.status_switch);
        }
    }
}

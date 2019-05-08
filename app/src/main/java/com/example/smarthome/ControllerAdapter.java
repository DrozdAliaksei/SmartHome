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

import java.util.ArrayList;
import java.util.List;

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
                Log.i(TAG,item.getDisplayable_name());
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

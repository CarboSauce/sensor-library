package com.sauce.sensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sauce.sensor.activities.SensorDetail;
import com.sauce.sensor.service.SensorService;

import java.util.List;
import java.util.regex.Pattern;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.ViewHolder> {

    private List<Sensor> sensorList;
    private Pattern typeRegex;
    private Context context;
    public SensorListAdapter(Context context) {
        this.sensorList = SensorService.Instance().getSensors();
        this.typeRegex = Pattern.compile("\\w+$");
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView sensorName;
        private TextView sensorType;
        private int index = -1;
        public ViewHolder(final View view) {
            super(view);
            sensorName = view.findViewById(R.id.sensorName);
            sensorType = view.findViewById(R.id.sensorType);
            itemView.setOnClickListener(this);
        }

        public void setIndex(final int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (index != -1) {
                var intent = new Intent(context, SensorDetail.class);
                intent.putExtra(SensorDetail.KEY_SENSOR_INDEX,index);
                context.startActivity(intent);
            }
        }
    }

    @NonNull
    @Override
    public SensorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.sensor_list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorListAdapter.ViewHolder holder, int position) {
        var name = sensorList
                .get(position)
                .getName();
        var type = SensorService
                .getPrettySensorType(sensorList.get(position));
        holder.setIndex(position);
        holder.sensorName.setText(name);
        holder.sensorType.setText(type);
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }
}

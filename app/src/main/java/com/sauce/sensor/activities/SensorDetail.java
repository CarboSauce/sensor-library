package com.sauce.sensor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.os.Bundle;
import android.widget.TextView;

import com.sauce.sensor.R;
import com.sauce.sensor.service.SensorService;

public class SensorDetail extends AppCompatActivity {
    public static final String KEY_SENSOR_INDEX = "SENSOR_INDEX";
    Sensor sensor;
    TextView sensorDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        var intent = getIntent();
        var sensorIndex = intent.getIntExtra(KEY_SENSOR_INDEX,-1);
        if (sensorIndex == -1) {
            finishActivity(1);
            return;
        }
        sensor = SensorService.Instance().getSensors().get(sensorIndex);

        fillViews();
    }

    void fillViews() {
        sensorDetailName = findViewById(R.id.sensorName);
        sensorDetailName.setText(sensor.getName());
    }
}
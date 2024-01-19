package com.sauce.sensor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.sauce.sensor.R;
import com.sauce.sensor.service.SensorService;

public class SensorDetail extends AppCompatActivity {
    public static final String KEY_SENSOR_INDEX = "SENSOR_INDEX";
    Sensor sensor;
    TextView sensorName;
    TextView sensorVendor;
    TextView sensorType;
    TextView sensorTypeCode;
    TextView sensorVersion;
    TextView sensorPower;
    TextView sensorMaxRange;
    TextView sensorResolution;
    TextView sensorMinDelay;
    TextView sensorMaxDelay;
    TextView sensorIsWakeUp;
    TextView sensorIsDynamic;
    TextView sensorIdentifier;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        var inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var id = item.getItemId();
        if (R.id.find_sensor_online == id) {
            var intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(
                    SearchManager.QUERY,
                    SensorService.sensorSearchQuery(sensor)
            );
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void fillViews() {
        sensorName = findViewById(R.id.sensorName);
        sensorName.setText(sensor.getName());

        sensorVendor = findViewById(R.id.sensorVendor);
        sensorVendor.setText(sensor.getVendor());

        sensorType = findViewById(R.id.sensorType);
        sensorType.setText(SensorService.getPrettySensorType(sensor));

        sensorTypeCode = findViewById(R.id.sensorTypeCode);
        sensorTypeCode.setText(String.valueOf(sensor.getType()));

        sensorVersion = findViewById(R.id.sensorVersion);
        sensorVersion.setText(String.valueOf(sensor.getVersion()));

        sensorPower = findViewById(R.id.sensorPower);
        sensorPower.setText(String.valueOf(sensor.getPower()));

        sensorMaxRange = findViewById(R.id.sensorMaxRange);
        sensorMaxRange.setText(String.valueOf(sensor.getMaximumRange()));

        sensorResolution = findViewById(R.id.sensorResolution);
        sensorResolution.setText(String.valueOf(sensor.getResolution()));

        sensorMinDelay = findViewById(R.id.sensorMinDelay);
        sensorMinDelay.setText(String.valueOf(sensor.getMinDelay()));

        sensorMaxDelay = findViewById(R.id.sensorMaxDelay);
        sensorMaxDelay.setText(String.valueOf(sensor.getMaxDelay()));

        sensorIsWakeUp = findViewById(R.id.sensorIsWakeUp);
        sensorIsWakeUp.setText(String.valueOf(sensor.isWakeUpSensor()));

        sensorIsDynamic = findViewById(R.id.sensorIsDynamic);
        sensorIsDynamic.setText(String.valueOf(sensor.isDynamicSensor()));

        sensorIdentifier = findViewById(R.id.sensorId);
        sensorIdentifier.setText(String.valueOf(sensor.getId()));
    }
}
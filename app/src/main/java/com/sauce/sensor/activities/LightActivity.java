package com.sauce.sensor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sauce.sensor.R;
import com.sauce.sensor.service.SensorService;
import com.sauce.sensor.util.ChartFloatFormatter;

import java.util.ArrayList;

public class LightActivity extends AppCompatActivity implements SensorEventListener {
    Sensor sensor;
    SensorManager sensorManager;
    TextView textView;
    Button toggleUpdate;
    Button resetChart;
    LineChart chart;
    static ArrayList<Float> sensorValues;
    ArrayList<Entry> entries;
    LineDataSet lineDataSet;
    LineData lineData;

    boolean updateChart = true;
    private final static String KEY_PAUSE_STATE = "pause_state";
    private final static int sampleCap = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        if (sensor == null) {
            sensorManager = SensorService
                    .Instance().getSensorManager();
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        textView = findViewById(R.id.lightTextView);
        chart = findViewById(R.id.lightChart);

        chart.setDrawGridBackground(true);
        chart.getDescription().setEnabled(false);

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);

        var l = chart.getLegend();
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextColor(Color.WHITE);


        if (sensorValues == null) {
            sensorValues = new ArrayList<Float>();
        }

        if (entries == null) {
            entries = new ArrayList<>();
            lineDataSet = new LineDataSet(entries,getString(R.string.light));
            lineDataSet.setColor(Color.RED);
            lineDataSet.setValueTextSize(11f);
            lineDataSet.setValueFormatter(new ChartFloatFormatter());
            lineData = new LineData(lineDataSet);
            chart.setData(lineData);
            fillValues();
        }

        toggleUpdate = findViewById(R.id.toggle_update);
        toggleUpdate.setOnClickListener(v -> {
            setToggleUpdate(!updateChart);
        });

        if (savedInstanceState != null) {
            var update = savedInstanceState.getBoolean(KEY_PAUSE_STATE);
            setToggleUpdate(update);
        } else {
            setToggleUpdate(true);
        }


        resetChart = findViewById(R.id.reset_chart);
        resetChart.setOnClickListener(v -> {
            this.resetChart();
        });

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_PAUSE_STATE,updateChart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensor != null) {
            sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            var sensorValue = event.values[0];
            var displayStr = String.format(
                    getString(R.string.light_sensor_text) + " = %.4f",sensorValue);
            textView.setText(displayStr);

            addValue(sensorValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void addValue(float value) {
        if (updateChart == false) return;
        if (sensorValues.size() < sampleCap) {
            sensorValues.add(value);
        } else {
            sensorValues.remove(0);
            sensorValues.add(value);
        }
        entries.clear();
        fillValues();
    }

    void fillValues() {
        for (int i = 0; i < sensorValues.size(); i++) {
            entries.add(new Entry(i,sensorValues.get(i)));
        }
        lineDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    void setToggleUpdate(boolean update) {
        updateChart = update;
        if (updateChart) {
            toggleUpdate.setText(R.string.stop);
        } else {
            toggleUpdate.setText(R.string.start);
        }
    }

    void resetChart() {
        sensorValues.clear();
        entries.clear();
        lineDataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
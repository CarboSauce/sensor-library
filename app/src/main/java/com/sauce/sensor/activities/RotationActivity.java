package com.sauce.sensor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sauce.sensor.R;
import com.sauce.sensor.service.SensorService;
import com.sauce.sensor.util.ChartFloatFormatter;

import java.util.ArrayList;


public class RotationActivity extends AppCompatActivity implements SensorEventListener {
    Sensor sensor;
    SensorManager sensorManager;
    TextView textView;
    Button toggleUpdate;
    Button resetChart;
    LineChart chart;
    static ArrayList<Float> sensorValuesX;
    static ArrayList<Float> sensorValuesY;
    static ArrayList<Float> sensorValuesZ;
    ArrayList<Entry> entriesX;
    ArrayList<Entry> entriesY;
    ArrayList<Entry> entriesZ;
    ArrayList<ILineDataSet> lineDataSets;

    LineData lineData;
    boolean updateChart = true;
    private final static String KEY_PAUSE_STATE = "pause_state";
    private final static int sampleCap = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        if (sensor == null) {
            sensorManager = SensorService
                    .Instance().getSensorManager();
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }

        textView = findViewById(R.id.textView);
        toggleUpdate = findViewById(R.id.toggle_update);
        resetChart = findViewById(R.id.reset_chart);
        chart = findViewById(R.id.lineChart);
        setupChart();

        if (sensorValuesX == null && sensorValuesY == null && sensorValuesZ == null) {
            sensorValuesX = new ArrayList<Float>();
            sensorValuesY = new ArrayList<Float>();
            sensorValuesZ = new ArrayList<Float>();
        }

        if (entriesX == null && entriesY == null && entriesZ == null) {
            entriesX = new ArrayList<>();
            entriesY = new ArrayList<>();
            entriesZ = new ArrayList<>();
            lineDataSets = new ArrayList<>();
            lineDataSets.add(new LineDataSet(entriesX,"X"));
            ((LineDataSet)lineDataSets.get(0)).setColor(Color.BLUE);
            lineDataSets.get(0).setValueTextSize(11f);
            lineDataSets.get(0).setValueFormatter(new ChartFloatFormatter());

            lineDataSets.add(new LineDataSet(entriesY,"Y"));
            ((LineDataSet)lineDataSets.get(1)).setColor(Color.RED);
            lineDataSets.get(1).setValueTextSize(11f);
            lineDataSets.get(1).setValueFormatter(new ChartFloatFormatter());

            lineDataSets.add(new LineDataSet(entriesZ,"Z"));
            ((LineDataSet)lineDataSets.get(2)).setColor(Color.GREEN);
            lineDataSets.get(2).setValueTextSize(11f);
            lineDataSets.get(2).setValueFormatter(new ChartFloatFormatter());

            lineData = new LineData(lineDataSets);
            chart.setData(lineData);
            fillValues();
        }

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

    void setupChart() {
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
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            var sensorValueX = event.values[0];
            var sensorValueY = event.values[1];
            var sensorValueZ = event.values[2];
            var displayStr = String.format(
                    getString(R.string.rotation_sensor_text)
                            + ": X = %.4f; Y = %.4f; Z = %.4f",sensorValueX,sensorValueY,sensorValueZ);
            textView.setText(displayStr);

            addValues(sensorValueX,sensorValueY,sensorValueZ);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void addValues(float x,float y, float z) {
        if (updateChart == false) return;
        if (sensorValuesX.size() < sampleCap) {
            sensorValuesX.add(x);
        } else {
            sensorValuesX.remove(0);
            sensorValuesX.add(x);
        }

        if (sensorValuesY.size() < sampleCap) {
            sensorValuesY.add(y);
        } else {
            sensorValuesY.remove(0);
            sensorValuesY.add(y);
        }

        if (sensorValuesZ.size() < sampleCap) {
            sensorValuesZ.add(z);
        } else {
            sensorValuesZ.remove(0);
            sensorValuesZ.add(z);
        }
        entriesX.clear();
        entriesY.clear();
        entriesZ.clear();
        fillValues();
    }

    void fillValues() {
        for (int i = 0; i < sensorValuesX.size(); i++) {
            entriesX.add(new Entry(i,sensorValuesX.get(i)));
        }
        for (int i = 0; i < sensorValuesY.size(); i++) {
            entriesY.add(new Entry(i,sensorValuesY.get(i)));
        }
        for (int i = 0; i < sensorValuesZ.size(); i++) {
            entriesZ.add(new Entry(i,sensorValuesZ.get(i)));
        }

        ((LineDataSet)lineDataSets.get(0)).notifyDataSetChanged();
        ((LineDataSet)lineDataSets.get(1)).notifyDataSetChanged();
        ((LineDataSet)lineDataSets.get(2)).notifyDataSetChanged();

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
        sensorValuesX.clear();
        sensorValuesY.clear();
        sensorValuesZ.clear();
        entriesX.clear();
        entriesY.clear();
        entriesZ.clear();
        ((LineDataSet)lineDataSets.get(0)).notifyDataSetChanged();
        ((LineDataSet)lineDataSets.get(1)).notifyDataSetChanged();
        ((LineDataSet)lineDataSets.get(2)).notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }
}
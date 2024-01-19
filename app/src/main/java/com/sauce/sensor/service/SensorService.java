package com.sauce.sensor.service;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SensorService {

    List<Sensor> sensors = new ArrayList<>();
    @Nullable
    SensorManager sensorManager;
    private static SensorService _instance;

    public static SensorService Instance() {
        if (_instance == null) {
            _instance = new SensorService();
        }
        return _instance;
    }
    private SensorService() {}

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    public List<Sensor> getSensors() { return sensors; }
    public SensorManager getSensorManager() { return sensorManager; }
}

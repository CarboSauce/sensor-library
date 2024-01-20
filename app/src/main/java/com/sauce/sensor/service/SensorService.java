package com.sauce.sensor.service;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SensorService {

    List<Sensor> sensors = new ArrayList<>();
    @Nullable
    SensorManager sensorManager;
    private static SensorService _instance;
    private static Pattern typeRegex = Pattern.compile("\\w+$");

    public static SensorService Instance() {
        if (_instance == null) {
            _instance = new SensorService();
        }
        return _instance;
    }
    private SensorService() {

    }
    public boolean hasAccelerometer() {
        return hasSensor(Sensor.TYPE_ACCELEROMETER);
    }
    public boolean hasRotation() {
        return hasSensor(Sensor.TYPE_ROTATION_VECTOR);
    }
    public boolean hasProximity() {
        return hasSensor(Sensor.TYPE_PROXIMITY);
    }
    public boolean hasLight() {
        return hasSensor(Sensor.TYPE_LIGHT);
    }
    private boolean hasSensor(int id) {
        return sensorManager .getDefaultSensor(id) != null;
    }
    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
    }
    public static String getPrettySensorType(Sensor sensor) {
        var sensorType = sensor.getStringType();
        var matcher = typeRegex.matcher(sensorType);
        if (matcher.find()) {
            return matcher.group();
        }
        return sensorType;
    }
    public static String sensorSearchQuery(Sensor sensor) {
        var vendor = sensor.getVendor();
        var name = sensor.getName();
        name = name.replace("Wakeup","");
        name = name.replace("Non-wakeup","");
        return vendor + " " + name;
    }
    public List<Sensor> getSensors() { return sensors; }
    public SensorManager getSensorManager() { return sensorManager; }
}

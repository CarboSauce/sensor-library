package com.sauce.sensor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.sauce.sensor.R;
import com.sauce.sensor.activities.AccelerometerActivity;
import com.sauce.sensor.activities.LightActivity;
import com.sauce.sensor.activities.ProximityActivity;
import com.sauce.sensor.activities.RotationActivity;
import com.sauce.sensor.service.SensorService;


public class Examples extends Fragment {

    Button accelerometerButton;
    Button lightButton;
    Button proximityButton;
    Button rotationButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_examples, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
    }

    void setupButtons() {
        accelerometerButton = getView().findViewById(R.id.accelerometerButton);
        lightButton = getView().findViewById(R.id.lightButton);
        proximityButton = getView().findViewById(R.id.proximityButton);
        rotationButton = getView().findViewById(R.id.rotationButton);

        accelerometerButton.setOnClickListener(v -> {
            if (!SensorService.Instance().hasAccelerometer()) {
                displaySensorMissing();
                return;
            }
            var intent = new Intent(getActivity(), AccelerometerActivity.class);
            startActivity(intent);
        });

        lightButton.setOnClickListener(v -> {
            if (!SensorService.Instance().hasLight()) {
                displaySensorMissing();
                return;
            }
            var intent = new Intent(getActivity(), LightActivity.class);
            startActivity(intent);
        });

        proximityButton.setOnClickListener(v -> {
            if (!SensorService.Instance().hasProximity()) {
                displaySensorMissing();
                return;
            }
            var intent = new Intent(getActivity(), ProximityActivity.class);
            startActivity(intent);
        });

        rotationButton.setOnClickListener(v -> {
            if (!SensorService.Instance().hasRotation()) {
                displaySensorMissing();
                return;
            }
            var intent = new Intent(getActivity(), RotationActivity.class);
            startActivity(intent);
        });
    }

    void displaySensorMissing() {
        var snackbar = Snackbar.make(
                getView(),
                getString(R.string.sensor_missing),
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
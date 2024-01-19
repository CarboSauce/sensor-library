package com.sauce.sensor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sauce.sensor.R;
import com.sauce.sensor.SensorListAdapter;


public class Sensors extends Fragment {
    RecyclerView recyclerView;
    SensorListAdapter sensorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycleView();
    }

    void setupRecycleView() {
        var layoutManager = new LinearLayoutManager(getContext());
        recyclerView = getView().findViewById(R.id.sensorsRecycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (sensorAdapter == null) {
            sensorAdapter = new SensorListAdapter(getActivity());
            recyclerView.setAdapter(sensorAdapter);
        } else {
            sensorAdapter.notifyDataSetChanged();
        }
    }
}
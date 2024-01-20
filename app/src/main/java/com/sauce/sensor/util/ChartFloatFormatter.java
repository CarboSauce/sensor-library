package com.sauce.sensor.util;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class ChartFloatFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return String.format("%.1f",value);
    }
}

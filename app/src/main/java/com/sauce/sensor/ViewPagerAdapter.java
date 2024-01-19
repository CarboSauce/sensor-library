package com.sauce.sensor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sauce.sensor.fragments.Examples;
import com.sauce.sensor.fragments.Sensors;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return new Sensors();
            case 1:
                return new Examples();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

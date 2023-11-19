package com.finko;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentLiquidityRatiosBinding;
import com.finko.databinding.FragmentSettingsBinding;


public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    FragmentSettingsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        binding.setListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }
}
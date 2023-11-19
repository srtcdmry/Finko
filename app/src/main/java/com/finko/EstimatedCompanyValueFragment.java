package com.finko;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentEstimatedCompanyValueBinding;
import com.finko.databinding.FragmentLiquidityRatiosBinding;


public class EstimatedCompanyValueFragment extends BaseFragment implements View.OnClickListener {

    FragmentEstimatedCompanyValueBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEstimatedCompanyValueBinding.inflate(inflater, container, false);

        binding.setListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }
}
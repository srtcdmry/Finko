package com.finko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentFinancialDataInputBinding;


public class FinancialDataInputFragment extends BaseFragment implements View.OnClickListener {

    FragmentFinancialDataInputBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFinancialDataInputBinding.inflate(inflater, container, false);
        binding.setListener(this);
        getToolbarBackButton().setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }
}
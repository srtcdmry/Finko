package com.finko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentFinancialAnalysisBinding;


public class FinancialAnalysisFragment extends BaseFragment implements View.OnClickListener {

    FragmentFinancialAnalysisBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFinancialAnalysisBinding.inflate(inflater, container, false);
        binding.setListener(this);
        getToolbarBackButton().setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.liquidityRatiosBtn){

        } else if (v == binding.leverageRatiosBtn) {

        } else if (v == binding.profitabilityRatiosBtn) {

        } else if (v == binding.turnoverRatesBtn) {

        }
    }
}
package com.finko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

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
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_financialAnalysisFragment_to_liquidityRatiosFragment);
        } else if (v == binding.leverageRatiosBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_financialAnalysisFragment_to_leverageRatiosFragment);
        } else if (v == binding.profitabilityRatiosBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_financialAnalysisFragment_to_profitabilityRatiosFragment);
        } else if (v == binding.turnoverRatesBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_financialAnalysisFragment_to_turnoverRatesFragment);
        }
    }
}
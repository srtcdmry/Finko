package com.finko;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

import com.finko.databinding.FragmentHomeBinding;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.financialInputBtn){
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_financialDataInputFragment);
        } else if (v == binding.financialAnalysisBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_financialAnalysisFragment);
        } else if (v == binding.estimatedCompanyBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_estimatedCompanyValueFragment);
        } else if (v == binding.financialStatementAnalysisBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_financialStatementAnalysisFragment);
        } else if (v == binding.sectorsBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_sectorsFragment);
        } else if (v == binding.logoutBtn) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_settingsFragment);
        }
    }
}
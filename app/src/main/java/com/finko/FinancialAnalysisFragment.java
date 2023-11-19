package com.finko;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentFinancialAnalysisBinding;


public class FinancialAnalysisFragment extends Fragment implements View.OnClickListener {

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

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

    }
}
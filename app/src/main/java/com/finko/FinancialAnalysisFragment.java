package com.finko;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finko.databinding.FragmentFinancialAnalysisBinding;


public class FinancialAnalysisFragment extends Fragment {

    FragmentFinancialAnalysisBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFinancialAnalysisBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
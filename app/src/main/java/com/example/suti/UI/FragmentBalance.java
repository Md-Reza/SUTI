package com.example.suti.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.suti.R;

public class FragmentBalance extends Fragment {

    private int calsBurned = 2;
    private int calsConsumed = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_balance, container, false);


        // Calculate the slice size and update the pie chart:
        ProgressBar pieChart = root.findViewById(R.id.stats_progressbar);
        double d = (double) calsBurned / (double) calsConsumed;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);

        return root;
    }
}
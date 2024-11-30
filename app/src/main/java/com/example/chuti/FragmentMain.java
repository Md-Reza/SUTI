package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.chuti.UI.EmployeeGatepassFragment;
import com.example.chuti.UI.FragmentBalance;


public class FragmentMain extends Fragment {

    LinearLayout mnuEmpGatePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mnuEmpGatePass = root.findViewById(R.id.mnuEmpGatePass);
        mnuEmpGatePass.setOnClickListener(v -> replaceFragment(new EmployeeGatepassFragment(), getContext()));

        return root;
    }
}
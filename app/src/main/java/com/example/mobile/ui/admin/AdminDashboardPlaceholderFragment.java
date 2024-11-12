package com.example.mobile.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mobile.AdminDashboardActivity;
import com.example.mobile.R;

public class AdminDashboardPlaceholderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Start AdminDashboardActivity
        Intent intent = new Intent(requireContext(), AdminDashboardActivity.class);
        startActivity(intent);

        // Close this placeholder fragment after launching the activity
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }

        // Inflate a minimal layout (or return null if not needed)
        return inflater.inflate(R.layout.fragment_placeholder, container, false);
    }
}

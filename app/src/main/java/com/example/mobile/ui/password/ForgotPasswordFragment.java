package com.example.mobile.ui.password;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mobile.Email.EmailSender;
import com.example.mobile.R;
import com.example.mobile.database.repositories.UserRepository;

import org.json.JSONObject;
import org.json.JSONException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executors;

public class ForgotPasswordFragment extends Fragment {

    private EditText emailEditText;
    private Button sendResetLinkButton;
    private UserRepository userRepo;

    private Handler handler;
    private Runnable checkResetStatusRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        sendResetLinkButton = view.findViewById(R.id.sendResetLinkButton);

        userRepo = new UserRepository(requireContext());
        handler = new Handler();

        sendResetLinkButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (!email.isEmpty()) {
                sendResetRequest(email);
            } else {
                Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendResetRequest(String email) {
        String url = "http://192.168.1.184:3000/request-password-reset";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    sendResetEmail(email);
                    startTrackingPasswordReset(email);
                },
                error -> {
                    error.printStackTrace(); // Print the full error

                    Toast.makeText(getActivity(), "Error sending reset request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );


        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void sendResetEmail(String email) {
        String subject = "Password Reset Request";
        String messageBody = "Click the link to reset your password: http://192.168.1.184:3000/reset-password?email=" + email;

        // Create an instance of EmailSender and send the email
        EmailSender emailSender = new EmailSender(email, subject, userRepo);
        emailSender.execute();

        Toast.makeText(getActivity(), "Reset email sent!", Toast.LENGTH_LONG).show();
    }

    private void startTrackingPasswordReset(String email) {
        checkResetStatusRunnable = new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.1.184:3000/check-password-reset?email=" + email;

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        response -> {
                            try {
                                boolean resetCompleted = response.getBoolean("resetCompleted");
                                if (resetCompleted) {

                                    // Retrieve the new password from the response
                                    String newPassword = response.getString("newPassword");

                                    // Store the new password in the user attribute (replace with actual storage logic)
                                    userRepo.updateUserPassword(email, newPassword);


                                    Toast.makeText(getActivity(), "Password has been reset successfully.", Toast.LENGTH_SHORT).show();
                                    handler.removeCallbacks(checkResetStatusRunnable); // Stop tracking


                                    // Navigate to the login page
                                    if (isAdded() && getView() != null) {
                                        Navigation.findNavController(getView()).navigate(R.id.loginFragment);
                                    }

                                } else {
                                    handler.postDelayed(checkResetStatusRunnable, 5000); // Retry after 5 seconds
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Toast.makeText(getActivity(), "Error checking reset status.", Toast.LENGTH_SHORT).show();
                            handler.removeCallbacks(checkResetStatusRunnable); // Stop tracking on error
                        }
                );

                RequestQueue queue = Volley.newRequestQueue(requireContext());
                queue.add(request);
            }
        };

        handler.post(checkResetStatusRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && checkResetStatusRunnable != null) {
            handler.removeCallbacks(checkResetStatusRunnable);
        }
    }
}

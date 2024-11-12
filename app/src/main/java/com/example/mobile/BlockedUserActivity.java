package com.example.mobile;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mobile.R;

public class BlockedUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_user);

        TextView blockedMessage = findViewById(R.id.textBlockedMessage);

        // Set a message to inform the user they are blocked
        blockedMessage.setText("Your account has been blocked. Please contact support for further assistance. Call 55 290 426");
    }
}

package com.example.toytrade2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class LogInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        Fragment selectedFragment = new LogInFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.log_in_frame,
                selectedFragment).commit();
    }
}

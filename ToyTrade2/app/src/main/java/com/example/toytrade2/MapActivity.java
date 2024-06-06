package com.example.toytrade2;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MapActivity extends AppCompatActivity implements MapFragment.OnButtonClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_with_back);
        Fragment selectedFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                selectedFragment).commit();
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onButtonClicked() {
        finish();
    }
}

package com.example.toytrade2;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AddToyActivity extends AppCompatActivity implements AddToyFragment.OnButtonClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_with_back);
        String source = getIntent().getStringExtra("source");
        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        Fragment selectedFragment = new AddToyFragment();
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selectedFragment).commit();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onButtonClicked() {
        finish();
    }
}

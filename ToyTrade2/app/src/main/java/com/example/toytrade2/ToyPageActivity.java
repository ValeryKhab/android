package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicReference;

public class ToyPageActivity extends AppCompatActivity implements ToyPageFragment.OnButtonClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_with_back);
        String source = getIntent().getStringExtra("source");
        AtomicReference<String> source1 = new AtomicReference<>("editToy");
        if (source != null) {
            if (source.equals("fromActiveToys")) {
                ImageButton editToyButton = new ImageButton(this);
                editToyButton.setImageResource(R.drawable.edit);
                editToyButton.setBackgroundResource(R.color.mint);
                int padding = 16;
                editToyButton.setPadding(padding, padding, padding, padding);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                params.weight = 1;
                editToyButton.setLayoutParams(params);
                LinearLayout topLinearLayout = findViewById(R.id.topLinearLayout);
                topLinearLayout.addView(editToyButton);
                editToyButton.setOnClickListener(v -> {
                    source1.set("editToy");
                    Intent intent = new Intent(v.getContext(), AddToyActivity.class);
                    intent.putExtra("source", source1.toString());
                    v.getContext().startActivity(intent);
                });
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        Fragment selectedFragment = new ToyPageFragment();
        selectedFragment.setArguments(bundle);
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

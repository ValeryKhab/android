package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class AskForChangeFragment extends Fragment {

    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_for_change, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        User user = dbcom.getUserById(currentUserId);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int blueColor = resources.getColor(R.color.blue);
        int whiteColor = resources.getColor(R.color.white);

        LinearLayout toyForChangeLinearLayout = view.findViewById(R.id.toyForChangeLinearLayout);
        toyForChangeLinearLayout.removeAllViews();
        List<Integer> activeToys = dbcom.getAllUserActiveToyIds(currentUserId, true);
        if (!activeToys.isEmpty()) {
            for (int toyId : activeToys) {
                Toy toy = dbcom.getToyById(toyId);
                LinearLayout linearLayout = new LinearLayout(view.getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.weight = 2;
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(8, 8, 8, 8);
                linearLayout.setBackgroundColor(whiteColor);
                ImageView toyImageView = new ImageView(view.getContext());
                toyImageView.setImageResource(R.drawable.toy);
                toyImageView.setBackgroundColor(blueColor);
                toyImageView.setLayoutParams(layoutParams);
                toyImageView.setMinimumHeight(200);
                TextView toyTextView = new TextView(view.getContext());
                toyTextView.setLayoutParams(layoutParams);
                toyTextView.setText(toy.getName());
                toyTextView.setTextSize(14);
                CheckBox toyCheckBox = new CheckBox(view.getContext());
                toyCheckBox.setLayoutParams(layoutParams1);
                toyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editor.putInt("toy_for_change", toyId);
                    editor.apply();
                });
                linearLayout.addView(toyImageView);
                linearLayout.addView(toyTextView);
                linearLayout.addView(toyCheckBox);
                toyForChangeLinearLayout.addView(linearLayout);
            }
        }

        Button askForChangeButton = view.findViewById(R.id.askForChangeButton);
        askForChangeButton.setOnClickListener(v -> {
            int changeToyId = sharedPreferences.getInt("toy_for_change", -1);
            int changeUserToyId = sharedPreferences.getInt("current_toy_id", -1);
            if (changeToyId != -1 && changeUserToyId != -1) {
                Toy changeToy = dbcom.getToyById(changeToyId);
                Trade trade = new Trade(currentUserId, changeToy.getUserId(), changeToyId, changeUserToyId, true);
                dbcom.addTrade(trade);
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}

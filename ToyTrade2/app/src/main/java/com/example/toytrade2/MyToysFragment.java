package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MyToysFragment extends Fragment {
    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_toys, container, false);
        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Button activeToysButton = view.findViewById(R.id.activeToysButton);
        Button archiveToysButton = view.findViewById(R.id.archiveToysButton);
        ImageButton newToyButton = view.findViewById(R.id.newToyButton);
        LinearLayout myToysLinearLayout = view.findViewById(R.id.myToysLinearLayout);

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int blueColor = resources.getColor(R.color.blue);
        int whiteColor = resources.getColor(R.color.white);

        AtomicReference<String> source = new AtomicReference<>("fromActiveToys");

        AtomicReference<String> source1 = new AtomicReference<>("editToy");

        activeToysButton.setOnClickListener(v -> {
            activeToysButton.setBackgroundColor(blueColor);
            archiveToysButton.setBackgroundColor(Color.WHITE);
            source.set("fromActiveToys");
            myToysLinearLayout.removeAllViews();
            List<Integer> activeToys = dbcom.getAllUserActiveToyIds(currentUserId, true);
            if (!activeToys.isEmpty()) {
                for (int toyId : activeToys) {
                    Toy toy = dbcom.getToyById(toyId);
                    Log.d("toyname", toy.getName());
                    LinearLayout linearLayout = new LinearLayout(v.getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setPadding(8, 8, 8, 8);
                    linearLayout.setBackgroundColor(whiteColor);
                    ImageView toyImageView = new ImageView(v.getContext());
                    toyImageView.setImageResource(R.drawable.toy);
                    toyImageView.setBackgroundColor(blueColor);
                    toyImageView.setLayoutParams(layoutParams);
                    toyImageView.setMinimumHeight(200);
                    TextView toyTextView = new TextView(v.getContext());
                    toyTextView.setLayoutParams(layoutParams);
                    toyTextView.setText(toy.getName());
                    toyTextView.setTextSize(14);
                    toyTextView.setPadding(8, 4, 4, 4);
                    toyTextView.setOnClickListener(v1 -> {
                        editor.putInt("current_toy_id", toyId);
                        editor.apply();
                        Intent intent = new Intent(v1.getContext(), ToyPageActivity.class);
                        intent.putExtra("source", source.toString());
                        v1.getContext().startActivity(intent);
                    });
                    LinearLayout.LayoutParams layoutParamsBut = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.weight = 2;
                    ImageButton toyImageButton = new ImageButton(v.getContext());
                    toyImageButton.setImageResource(R.drawable.edit);
                    toyImageButton.setLayoutParams(layoutParamsBut);
                    toyImageButton.setBackgroundColor(blueColor);
                    toyImageButton.setOnClickListener(v1 -> {
                        editor.putInt("current_toy_id", toyId);
                        editor.apply();
                        source1.set("editToy");
                        Intent intent = new Intent(v1.getContext(), AddToyActivity.class);
                        intent.putExtra("source", source1.toString());
                        v1.getContext().startActivity(intent);
                    });
                    linearLayout.addView(toyImageView);
                    linearLayout.addView(toyTextView);
                    linearLayout.addView(toyImageButton);
                    myToysLinearLayout.addView(linearLayout);
                }
            }
        });

        archiveToysButton.setOnClickListener(v -> {
            activeToysButton.setBackgroundColor(Color.WHITE);
            archiveToysButton.setBackgroundColor(blueColor);
            source.set("fromArchiveToys");
            myToysLinearLayout.removeAllViews();
            List<Integer> inactiveToys = dbcom.getAllUserActiveToyIds(currentUserId, false);
            if (!inactiveToys.isEmpty()) {
                for (int toyId : inactiveToys) {
                    Toy toy = dbcom.getToyById(toyId);
                    LinearLayout linearLayout = new LinearLayout(v.getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.weight = 1;
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setPadding(8, 8, 8, 8);
                    linearLayout.setBackgroundColor(whiteColor);
                    ImageView toyImageView = new ImageView(v.getContext());
                    toyImageView.setImageResource(R.drawable.toy);
                    toyImageView.setBackgroundColor(blueColor);
                    toyImageView.setLayoutParams(layoutParams);
                    toyImageView.setMinimumHeight(200);
                    TextView toyTextView = new TextView(v.getContext());
                    toyTextView.setLayoutParams(layoutParams);
                    toyTextView.setText(toy.getName());
                    toyTextView.setTextSize(14);
                    toyTextView.setPadding(8, 4, 4, 4);
                    toyTextView.setOnClickListener(v1 -> {
                        editor.putInt("current_toy_id", toyId);
                        editor.apply();
                        Intent intent = new Intent(v1.getContext(), ToyPageActivity.class);
                        intent.putExtra("source", source.toString());
                        v1.getContext().startActivity(intent);
                    });
                    LinearLayout.LayoutParams layoutParamsBut = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.weight = 2;
                    ImageButton toyImageButton = new ImageButton(v.getContext());
                    toyImageButton.setImageResource(R.drawable.delete);
                    toyImageButton.setBackgroundColor(blueColor);
                    toyImageButton.setLayoutParams(layoutParamsBut);
                    toyImageButton.setOnClickListener(v1 -> {
                        dbcom.deleteToy(toyId);
                        myToysLinearLayout.removeView((View) v1.getParent());
                    });
                    linearLayout.addView(toyImageView);
                    linearLayout.addView(toyTextView);
                    linearLayout.addView(toyImageButton);
                    myToysLinearLayout.addView(linearLayout);
                }
            }
        });

        newToyButton.setOnClickListener(v -> {
            source1.set("addToy");
            Intent intent = new Intent(v.getContext(), AddToyActivity.class);
            intent.putExtra("source", source1.toString());
            v.getContext().startActivity(intent);
        });

        activeToysButton.callOnClick();

        return view;
    }
}

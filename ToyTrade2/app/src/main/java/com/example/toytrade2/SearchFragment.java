package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class SearchFragment extends Fragment {

    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        dbcom = new DBCommands(view.getContext());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        String currentSearchText = sharedPreferences.getString("current_search_text", "");

        SharedPreferences.Editor editor = sharedPreferences.edit();

        ImageButton backButton = view.findViewById(R.id.imageButtonBack);
        backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_frame, mainFragment);
                transaction.commit();
            }
        });

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int whiteColor = resources.getColor(R.color.white);
        int blackColor = resources.getColor(R.color.black);

        TableLayout toysTableLayout = view.findViewById(R.id.searchToysTableLayout);
        List<Integer> toysIds = dbcom.getSearchToys(currentSearchText, currentUserId);

        if (!toysIds.isEmpty()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            TableRow tableRow = new TableRow(view.getContext());
            LinearLayout linearLayout = new LinearLayout(view.getContext());
            for (int i = 0; i <= toysIds.size(); i++) {
                int toyId = toysIds.get(i);
                Toy toy = dbcom.getToyById(toyId);
                if (toyId % 2 == 1) {
                    tableRow = new TableRow(view.getContext());
                    linearLayout = new LinearLayout(view.getContext());
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    tableRow.setLayoutParams(layoutParams);
                }
                LinearLayout toyLinearLayout = new LinearLayout(view.getContext());
                LinearLayout.LayoutParams toyLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                toyLinearLayout.setLayoutParams(toyLayoutParams);
                toyLinearLayout.setOrientation(LinearLayout.VERTICAL);
                toyLinearLayout.setPadding(8, 8, 8, 8);
                toyLinearLayout.setBackgroundColor(whiteColor);
                ImageView toyImageView = new ImageView(view.getContext());
                TextView toyNametextView = new TextView(view.getContext());
                toyNametextView.setText(toy.getName());
                toyNametextView.setLayoutParams(layoutParams);
                toyNametextView.setTextSize(14);
                toyNametextView.setTextColor(blackColor);
                TextView toyAddressTextView = new TextView(view.getContext());
                Address toyAddress = dbcom.getAddressById(toy.getAddressId());
                toyAddressTextView.setText(toyAddress.getAddress());
                toyAddressTextView.setLayoutParams(layoutParams);
                toyAddressTextView.setTextSize(12);
                TextView toyDateTextVies = new TextView(view.getContext());
                toyDateTextVies.setTextSize(12);
                toyDateTextVies.setLayoutParams(layoutParams);

                toyNametextView.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), ToyPageActivity.class);
                    intent.putExtra("source", "fromMainPage");
                    v.getContext().startActivity(intent);
                });

                toyLinearLayout.addView(toyImageView);
                toyLinearLayout.addView(toyNametextView);
                toyLinearLayout.addView(toyAddressTextView);
                toyLinearLayout.addView(toyDateTextVies);
                linearLayout.addView(toyLinearLayout);
                if (i % 2 == 0) {
                    tableRow.addView(linearLayout);
                    toysTableLayout.addView(tableRow);
                }
            }
        }

        ImageButton searchButton = view.findViewById(R.id.imageButtonSearch);
        EditText searchEditTextText = view.findViewById(R.id.searchEditTextText);

        searchButton.setOnClickListener(v -> {
            String searchText = searchEditTextText.getText().toString();
            SearchFragment searchFragment = new SearchFragment();
            editor.putString("current_search_text", searchText);
            editor.apply();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_frame, searchFragment);
            transaction.commit();
        });

        return view;
    }
}

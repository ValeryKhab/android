package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment {
    private DBCommands dbcom;
    private static final String API_KEY = "0912fc6d21b048649974d0fa95d4f602";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        dbcom = new DBCommands(view.getContext());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int whiteColor = resources.getColor(R.color.white);
        int blackColor = resources.getColor(R.color.black);
        int blueColor = resources.getColor(R.color.blue);

        TextView holidayTextView = view.findViewById(R.id.holidayTextView);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        HolidaysApi apiServise = ApiClient.getRetrofitInstance().create(HolidaysApi.class);
        Call<List<Holiday>> call = apiServise.getHolidays(API_KEY, "RU", year, month, day);
        call.enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                if (response.isSuccessful()) {
                    List<Holiday> holidays = response.body();
                    if (holidays != null && !holidays.isEmpty()) {
                        StringBuilder holidaysText = new StringBuilder();
                        for (Holiday holiday : holidays) {
                            holidaysText.append("Праздник: ").append(holiday.getName())
                                        .append("\nДата: ").append(holiday.getDate())
                                        .append("\nстрана: ").append(holiday.getCountry())
                                        .append("\n\n");
                        }
                        holidayTextView.setText(holidaysText.toString());
                    } else {
                        holidayTextView.setText("Сегодня праздников нет");
                    }
                } else {
                    holidayTextView.setText("Не удалось получить данные: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Holiday>> call, Throwable t) {
                holidayTextView.setText("Ошибка: " + t.getMessage());
            }
        });


//        TableLayout toysTableLayout = view.findViewById(R.id.toysTableLayout);
//        toysTableLayout.removeAllViews();
        LinearLayout toysLinearLayout = view.findViewById(R.id.toysLinearLayout);
        toysLinearLayout.removeAllViews();
        List<Integer> toysIds = dbcom.getAllActiveToyIds(currentUserId);

        if (!toysIds.isEmpty()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
//            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            TableRow tableRow = new TableRow(view.getContext());
//            for (int i = 0; i < toysIds.size(); i++) {
//                int toyId = toysIds.get(i);
//                Toy toy = dbcom.getToyById(toyId);
//                if (i % 2 == 0) {
//                    tableRow = new TableRow(view.getContext());
//                    linearLayout = new LinearLayout(view.getContext());
//                    linearLayout.setLayoutParams(layoutParams);
//                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    tableRow.setLayoutParams(layoutParams1);
//                }
//                LinearLayout toyLinearLayout = new LinearLayout(view.getContext());
//                LinearLayout.LayoutParams toyLayoutParams = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                );
//                toyLayoutParams.weight = 1;
//                toyLinearLayout.setLayoutParams(toyLayoutParams);
//                toyLinearLayout.setOrientation(LinearLayout.VERTICAL);
//                toyLinearLayout.setPadding(8, 8, 8, 8);
//                toyLinearLayout.setBackgroundColor(whiteColor);
//                ImageView toyImageView = new ImageView(view.getContext());
//                toyImageView.setImageResource(R.drawable.toy);
//                toyImageView.setBackgroundColor(blueColor);
//                toyImageView.setLayoutParams(layoutParams);
//                TextView toyNametextView = new TextView(view.getContext());
//                toyNametextView.setText(toy.getName());
//                toyNametextView.setLayoutParams(layoutParams);
//                toyNametextView.setTextSize(14);
//                toyNametextView.setTextColor(blackColor);
//                TextView toyAddressTextView = new TextView(view.getContext());
//                Address toyAddress = dbcom.getAddressById(toy.getAddressId());
//                if (toyAddress != null) {
//                    toyAddressTextView.setText(toyAddress.getAddress());
//                }
//                toyAddressTextView.setLayoutParams(layoutParams);
//                toyAddressTextView.setTextSize(12);
//
//                toyNametextView.setOnClickListener(v -> {
//                    Intent intent = new Intent(v.getContext(), ToyPageActivity.class);
//                    editor.putInt("current_toy_id", toyId);
//                    editor.apply();
//                    intent.putExtra("source", "fromMainPage");
//                    v.getContext().startActivity(intent);
//                });
//
//                toyLinearLayout.addView(toyImageView);
//                toyLinearLayout.addView(toyNametextView);
//                toyLinearLayout.addView(toyAddressTextView);
////                toyLinearLayout.addView(toyDateTextVies);
//                linearLayout.addView(toyLinearLayout);
//                if (i % 2 == 0) {
//                    tableRow.addView(linearLayout);
//                    toysTableLayout.addView(tableRow);
//                }
//            }
            for (int toyId : toysIds) {
                LinearLayout linearLayout = new LinearLayout(view.getContext());
                Toy toy = dbcom.getToyById(toyId);
                Log.d("toyname", toy.getName());
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(16, 16, 16, 16);
                linearLayout.setBackgroundColor(whiteColor);
                ImageView toyImageView = new ImageView(view.getContext());
                toyImageView.setImageResource(R.drawable.toy);
                toyImageView.setBackgroundColor(blueColor);
                toyImageView.setLayoutParams(layoutParams);
                toyImageView.setMinimumHeight(300);
                TextView toyTextView = new TextView(view.getContext());
                toyTextView.setLayoutParams(layoutParams);
                toyTextView.setText(toy.getName());
                toyTextView.setTextSize(18);
                toyTextView.setPadding(4, 4, 4, 4);
                toyTextView.setOnClickListener(v1 -> {
                    editor.putInt("current_toy_id", toyId);
                    editor.apply();
                    Intent intent = new Intent(v1.getContext(), ToyPageActivity.class);
                    intent.putExtra("source", "fromMainPage");
                    v1.getContext().startActivity(intent);
                });
                TextView userTextView = new TextView(view.getContext());
                userTextView.setLayoutParams(layoutParams);
                User user = dbcom.getUserById(toy.getUserId());
                userTextView.setText(user.getName());
                userTextView.setTextSize(14);
                userTextView.setPadding(4, 4, 4, 4);
                linearLayout.addView(toyImageView);
                linearLayout.addView(toyTextView);
                linearLayout.addView(userTextView);
                toysLinearLayout.addView(linearLayout);
            }
        }

        ImageButton searchButton = view.findViewById(R.id.imageButtonSearch);
        EditText searchEditTextText = view.findViewById(R.id.searchEditTextText);

        searchButton.setOnClickListener(v -> {
            String searchText = searchEditTextText.getText().toString();
            editor.putString("current_search_text", searchText);
            editor.apply();
            SearchFragment searchFragment = new SearchFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_frame, searchFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}

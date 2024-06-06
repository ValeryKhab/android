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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class UserPageFragment extends Fragment {

    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_page, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_toy_user", -1);
        User user = dbcom.getUserById(currentUserId);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        ImageView userImageView = view.findViewById(R.id.userImageView);
        TextView userNameTextView = view.findViewById(R.id.userNameTextView);
        userNameTextView.setText(user.getName());
        TextView userPhoneTextView = view.findViewById(R.id.userPhoneTextView);
        userPhoneTextView.setText(user.getPhone());
        TextView userEmailTextView = view.findViewById(R.id.userEmailTextView);
        userEmailTextView.setText(user.getEmail());
        LinearLayout userLinearLayout = view.findViewById(R.id.userLinearLayout);
        userLinearLayout.removeAllViews();
//        TableLayout userTableLayout = view.findViewById(R.id.userTableLayout);
//        userTableLayout.removeAllViews();

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int whiteColor = resources.getColor(R.color.white);
        int blackColor = resources.getColor(R.color.black);
        int blueColor = resources.getColor(R.color.blue);

        List<Integer> toysIds = dbcom.getAllUserActiveToyIds(currentUserId, true);

        if (!toysIds.isEmpty()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
//            TableRow tableRow = new TableRow(view.getContext());
//            for (int i = 0; i < toysIds.size(); i++) {
//                int toyId = toysIds.get(i);
//                Toy toy = dbcom.getToyById(toyId);
//                if (i % 2 == 0) {
//                    tableRow = new TableRow(view.getContext());
//                    linearLayout = new LinearLayout(view.getContext());
//                    linearLayout.setLayoutParams(layoutParams);
//                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    tableRow.setLayoutParams(layoutParams);
//                }
//                LinearLayout toyLinearLayout = new LinearLayout(view.getContext());
//                LinearLayout.LayoutParams toyLayoutParams = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                );
//                toyLinearLayout.setLayoutParams(toyLayoutParams);
//                toyLinearLayout.setOrientation(LinearLayout.VERTICAL);
//                toyLinearLayout.setPadding(8, 8, 8, 8);
//                toyLinearLayout.setBackgroundColor(whiteColor);
//                ImageView toyImageView = new ImageView(view.getContext());
//                toyImageView.setImageResource(R.drawable.toy);
//                toyImageView.setLayoutParams(layoutParams);
//                toyImageView.setBackgroundColor(blueColor);
//                TextView toyNametextView = new TextView(view.getContext());
//                toyNametextView.setText(toy.getName());
//                toyNametextView.setLayoutParams(layoutParams);
//                toyNametextView.setTextSize(14);
//                toyNametextView.setTextColor(blackColor);
//                TextView toyAddressTextView = new TextView(view.getContext());
//                Address toyAddress = dbcom.getAddressById(toy.getAddressId());
////                toyAddressTextView.setText(toyAddress.getAddress());
//                toyAddressTextView.setLayoutParams(layoutParams);
//                toyAddressTextView.setTextSize(12);
////                TextView toyDateTextView = new TextView(view.getContext());
////                toyDateTextView.setTextSize(12);
////                toyDateTextView.setLayoutParams(layoutParams);
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
////                toyLinearLayout.addView(toyDateTextView);
//                linearLayout.addView(toyLinearLayout);
//                if (i % 2 == 0) {
//                    tableRow.addView(linearLayout);
//                    userTableLayout.addView(tableRow);
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
                linearLayout.addView(toyImageView);
                linearLayout.addView(toyTextView);
                userLinearLayout.addView(linearLayout);
            }
        }

        return view;
    }
}

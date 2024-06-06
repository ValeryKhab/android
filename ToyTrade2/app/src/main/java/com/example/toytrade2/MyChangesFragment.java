package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class MyChangesFragment extends Fragment {

    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_changes, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Button requestChangesButton = view.findViewById(R.id.requestChangesButton);
        Button complitedChangesButton = view.findViewById(R.id.complitedChangesButton);

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int blueColor = resources.getColor(R.color.blue);
        int blackColor = resources.getColor(R.color.black);
        int mintColor = resources.getColor(R.color.mint);

        LinearLayout changesLinearLayout = view.findViewById(R.id.changesLinearLayout);
        changesLinearLayout.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        requestChangesButton.setOnClickListener(v -> {
            requestChangesButton.setBackgroundColor(blueColor);
            complitedChangesButton.setBackgroundColor(Color.WHITE);
            changesLinearLayout.removeAllViews();
            List<Integer> userTrades = dbcom.getAllUserTradeIds(currentUserId, true);
            if (!userTrades.isEmpty()) {
                for (int tradeId : userTrades) {
                    Trade trade = dbcom.getTradeById(tradeId);
                    Toy toy1 = dbcom.getToyById(trade.getToy1Id());
                    Toy toy2 = dbcom.getToyById(trade.getToy2Id());
                    User user = dbcom.getUserById(trade.getUser2Id());
                    LinearLayout tradeLinearLayout = new LinearLayout(v.getContext());
                    tradeLinearLayout.setLayoutParams(layoutParams);
                    tradeLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toysTradeLinearLayout = new LinearLayout(v.getContext());
                    toysTradeLinearLayout.setLayoutParams(layoutParams);
                    toysTradeLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout toyLinearLayout1 = new LinearLayout(v.getContext());
                    toyLinearLayout1.setLayoutParams(layoutParams);
                    toyLinearLayout1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toyLinearLayout2 = new LinearLayout(v.getContext());
                    toyLinearLayout2.setLayoutParams(layoutParams);
                    toyLinearLayout2.setOrientation(LinearLayout.VERTICAL);
                    ImageView changeImageView = new ImageView(v.getContext());
                    changeImageView.setLayoutParams(layoutParams);
                    changeImageView.setImageResource(R.drawable.change);
                    changeImageView.setColorFilter(blackColor);
                    changeImageView.setLayoutParams(layoutParams);
                    ImageView toyImage1 = new ImageView(v.getContext());
                    TextView toyName1 = new TextView(v.getContext());
                    toyName1.setLayoutParams(layoutParams);
                    toyName1.setTextSize(14);
                    toyName1.setText(toy1.getName());
                    ImageView toyImage2 = new ImageView(v.getContext());
                    TextView toyName2 = new TextView(v.getContext());
                    toyName2.setLayoutParams(layoutParams);
                    toyName2.setTextSize(14);
                    toyName2.setText(toy2.getName());
                    TextView toyUser = new TextView(v.getContext());
                    toyUser.setLayoutParams(layoutParams);
                    toyUser.setTextSize(12);
                    toyUser.setText(user.getName());
                    toyUser.setOnClickListener(v1 -> {
                        Intent intent = new Intent(v.getContext(), UserPageActivity.class);
                        editor.putInt("current_toy_user", trade.getUser2Id());
                        editor.apply();
                        v.getContext().startActivity(intent);
                    });
                    TextView changeRequest = new TextView(v.getContext());
                    changeRequest.setTextSize(12);
                    changeRequest.setText("Заявка пользователю отправлена");
                    changeRequest.setLayoutParams(layoutParams);
                    toyLinearLayout1.addView(toyImage1);
                    toyLinearLayout1.addView(toyName1);
                    toyLinearLayout2.addView(toyImage2);
                    toyLinearLayout2.addView(toyName2);
                    toyLinearLayout2.addView(toyUser);
                    toysTradeLinearLayout.addView(toyLinearLayout1);
                    toysTradeLinearLayout.addView(changeImageView);
                    toysTradeLinearLayout.addView(toyLinearLayout2);
                    tradeLinearLayout.addView(toysTradeLinearLayout);
                    tradeLinearLayout.addView(changeRequest);
                    changesLinearLayout.addView(tradeLinearLayout);
                }
            }
            List<Integer> toUserTrades = dbcom.getAllToUserTradeIds(currentUserId, true);
            if (!toUserTrades.isEmpty()) {
                for (int tradeId : toUserTrades) {
                    Trade trade = dbcom.getTradeById(tradeId);
                    Toy toy1 = dbcom.getToyById(trade.getToy1Id());
                    Toy toy2 = dbcom.getToyById(trade.getToy2Id());
                    User user = dbcom.getUserById(trade.getUser1Id());
                    LinearLayout tradeLinearLayout = new LinearLayout(v.getContext());
                    tradeLinearLayout.setLayoutParams(layoutParams);
                    tradeLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toysTradeLinearLayout = new LinearLayout(v.getContext());
                    toysTradeLinearLayout.setLayoutParams(layoutParams);
                    toysTradeLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout toyLinearLayout1 = new LinearLayout(v.getContext());
                    toyLinearLayout1.setLayoutParams(layoutParams);
                    toyLinearLayout1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toyLinearLayout2 = new LinearLayout(v.getContext());
                    toyLinearLayout2.setLayoutParams(layoutParams);
                    toyLinearLayout2.setOrientation(LinearLayout.VERTICAL);
                    ImageView changeImageView = new ImageView(v.getContext());
                    changeImageView.setLayoutParams(layoutParams);
                    changeImageView.setImageResource(R.drawable.change);
                    changeImageView.setColorFilter(blackColor);
                    changeImageView.setLayoutParams(layoutParams);
                    ImageView toyImage1 = new ImageView(v.getContext());
                    TextView toyName1 = new TextView(v.getContext());
                    toyName1.setLayoutParams(layoutParams);
                    toyName1.setTextSize(14);
                    toyName1.setText(toy1.getName());
                    ImageView toyImage2 = new ImageView(v.getContext());
                    TextView toyName2 = new TextView(v.getContext());
                    toyName2.setLayoutParams(layoutParams);
                    toyName2.setTextSize(14);
                    toyName2.setText(toy2.getName());
                    TextView toyUser = new TextView(v.getContext());
                    toyUser.setLayoutParams(layoutParams);
                    toyUser.setTextSize(12);
                    toyUser.setText(user.getName());
                    toyUser.setOnClickListener(v1 -> {
                        Intent intent = new Intent(v.getContext(), UserPageActivity.class);
                        editor.putInt("current_toy_user", trade.getUser1Id());
                        editor.apply();
                        v.getContext().startActivity(intent);
                    });
                    Button endChange = new Button(v.getContext());
                    endChange.setBackgroundColor(mintColor);
                    endChange.setText("Завершить");
                    endChange.setPadding(4, 4, 4, 4);
                    endChange.setLayoutParams(layoutParams);
                    endChange.setTextSize(12);
                    endChange.setOnClickListener(v1 -> {
                        dbcom.updateTradeStatus(tradeId, false);

                    });
                    toyLinearLayout1.addView(toyImage1);
                    toyLinearLayout1.addView(toyName1);
                    toyLinearLayout1.addView(toyUser);
                    toyLinearLayout2.addView(toyImage2);
                    toyLinearLayout2.addView(toyName2);
                    toysTradeLinearLayout.addView(toyLinearLayout1);
                    toysTradeLinearLayout.addView(changeImageView);
                    toysTradeLinearLayout.addView(toyLinearLayout2);
                    tradeLinearLayout.addView(toysTradeLinearLayout);
                    tradeLinearLayout.addView(endChange);
                    changesLinearLayout.addView(tradeLinearLayout);
                }
            }
        });

        complitedChangesButton.setOnClickListener(v -> {
            requestChangesButton.setBackgroundColor(Color.WHITE);
            complitedChangesButton.setBackgroundColor(blueColor);
            changesLinearLayout.removeAllViews();
            List<Integer> userTrades = dbcom.getAllUserTradeIds(currentUserId, false);
            if (!userTrades.isEmpty()) {
                for (int tradeId : userTrades) {
                    Trade trade = dbcom.getTradeById(tradeId);
                    Toy toy1 = dbcom.getToyById(trade.getToy1Id());
                    Toy toy2 = dbcom.getToyById(trade.getToy2Id());
                    User user = dbcom.getUserById(trade.getUser2Id());
                    LinearLayout tradeLinearLayout = new LinearLayout(v.getContext());
                    tradeLinearLayout.setLayoutParams(layoutParams);
                    tradeLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toysTradeLinearLayout = new LinearLayout(v.getContext());
                    toysTradeLinearLayout.setLayoutParams(layoutParams);
                    toysTradeLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout toyLinearLayout1 = new LinearLayout(v.getContext());
                    toyLinearLayout1.setLayoutParams(layoutParams);
                    toyLinearLayout1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toyLinearLayout2 = new LinearLayout(v.getContext());
                    toyLinearLayout2.setLayoutParams(layoutParams);
                    toyLinearLayout2.setOrientation(LinearLayout.VERTICAL);
                    ImageView changeImageView = new ImageView(v.getContext());
                    changeImageView.setLayoutParams(layoutParams);
                    changeImageView.setImageResource(R.drawable.change);
                    changeImageView.setColorFilter(blackColor);
                    changeImageView.setLayoutParams(layoutParams);
                    ImageView toyImage1 = new ImageView(v.getContext());
                    TextView toyName1 = new TextView(v.getContext());
                    toyName1.setLayoutParams(layoutParams);
                    toyName1.setTextSize(14);
                    toyName1.setText(toy1.getName());
                    ImageView toyImage2 = new ImageView(v.getContext());
                    TextView toyName2 = new TextView(v.getContext());
                    toyName2.setLayoutParams(layoutParams);
                    toyName2.setTextSize(14);
                    toyName2.setText(toy2.getName());
                    TextView toyUser = new TextView(v.getContext());
                    toyUser.setLayoutParams(layoutParams);
                    toyUser.setTextSize(12);
                    toyUser.setText(user.getName());
                    toyUser.setOnClickListener(v1 -> {
                        Intent intent = new Intent(v.getContext(), UserPageActivity.class);
                        editor.putInt("current_toy_user", trade.getUser2Id());
                        editor.apply();
                        v.getContext().startActivity(intent);
                    });
                    TextView changeResponse = new TextView(v.getContext());
                    changeResponse.setTextSize(12);
                    changeResponse.setText("Обмен завершен");
                    changeResponse.setLayoutParams(layoutParams);
                    toyLinearLayout1.addView(toyImage1);
                    toyLinearLayout1.addView(toyName1);
                    toyLinearLayout2.addView(toyImage2);
                    toyLinearLayout2.addView(toyName2);
                    toyLinearLayout2.addView(toyUser);
                    toysTradeLinearLayout.addView(toyLinearLayout1);
                    toysTradeLinearLayout.addView(changeImageView);
                    toysTradeLinearLayout.addView(toyLinearLayout2);
                    tradeLinearLayout.addView(toysTradeLinearLayout);
                    tradeLinearLayout.addView(changeResponse);
                    changesLinearLayout.addView(tradeLinearLayout);
                }
            }
            List<Integer> toUserTrades = dbcom.getAllToUserTradeIds(currentUserId, false);
            if (!toUserTrades.isEmpty()) {
                for (int tradeId : toUserTrades) {
                    Trade trade = dbcom.getTradeById(tradeId);
                    Toy toy1 = dbcom.getToyById(trade.getToy1Id());
                    Toy toy2 = dbcom.getToyById(trade.getToy2Id());
                    User user = dbcom.getUserById(trade.getUser1Id());
                    LinearLayout tradeLinearLayout = new LinearLayout(v.getContext());
                    tradeLinearLayout.setLayoutParams(layoutParams);
                    tradeLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toysTradeLinearLayout = new LinearLayout(v.getContext());
                    toysTradeLinearLayout.setLayoutParams(layoutParams);
                    toysTradeLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout toyLinearLayout1 = new LinearLayout(v.getContext());
                    toyLinearLayout1.setLayoutParams(layoutParams);
                    toyLinearLayout1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout toyLinearLayout2 = new LinearLayout(v.getContext());
                    toyLinearLayout2.setLayoutParams(layoutParams);
                    toyLinearLayout2.setOrientation(LinearLayout.VERTICAL);
                    ImageView changeImageView = new ImageView(v.getContext());
                    changeImageView.setLayoutParams(layoutParams);
                    changeImageView.setImageResource(R.drawable.change);
                    changeImageView.setColorFilter(blackColor);
                    changeImageView.setLayoutParams(layoutParams);
                    ImageView toyImage1 = new ImageView(v.getContext());
                    TextView toyName1 = new TextView(v.getContext());
                    toyName1.setLayoutParams(layoutParams);
                    toyName1.setTextSize(14);
                    toyName1.setText(toy1.getName());
                    ImageView toyImage2 = new ImageView(v.getContext());
                    TextView toyName2 = new TextView(v.getContext());
                    toyName2.setLayoutParams(layoutParams);
                    toyName2.setTextSize(14);
                    toyName2.setText(toy2.getName());
                    TextView toyUser = new TextView(v.getContext());
                    toyUser.setLayoutParams(layoutParams);
                    toyUser.setTextSize(12);
                    toyUser.setText(user.getName());
                    toyUser.setOnClickListener(v1 -> {
                        Intent intent = new Intent(v.getContext(), UserPageActivity.class);
                        editor.putInt("current_toy_user", trade.getUser2Id());
                        editor.apply();
                        v.getContext().startActivity(intent);
                    });
                    TextView changeResponse = new TextView(v.getContext());
                    changeResponse.setTextSize(12);
                    changeResponse.setText("Обмен завершен");
                    changeResponse.setLayoutParams(layoutParams);
                    toyLinearLayout1.addView(toyImage1);
                    toyLinearLayout1.addView(toyName1);
                    toyLinearLayout1.addView(toyUser);
                    toyLinearLayout2.addView(toyImage2);
                    toyLinearLayout2.addView(toyName2);
                    toysTradeLinearLayout.addView(toyLinearLayout1);
                    toysTradeLinearLayout.addView(changeImageView);
                    toysTradeLinearLayout.addView(toyLinearLayout2);
                    tradeLinearLayout.addView(toysTradeLinearLayout);
                    tradeLinearLayout.addView(changeResponse);
                    changesLinearLayout.addView(tradeLinearLayout);
                }
            }
        });

        requestChangesButton.callOnClick();

        return view;
    }
}

package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ToyPageFragment extends Fragment {
    DBCommands dbcom;
    public interface OnButtonClickListener {
        void onButtonClicked();
    }

    private OnButtonClickListener listener;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            listener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toy_page, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        User user = dbcom.getUserById(currentUserId);
        int currentToyId = sharedPreferences.getInt("current_toy_id", -1);
        Toy toy = dbcom.getToyById(currentToyId);

        ImageView photoValueImageView = view.findViewById(R.id.photoValueImageView);
        ImageButton photoPrevImageButton = view.findViewById(R.id.photoPrevImageButton);
        ImageButton photoNextImageButton = view.findViewById(R.id.photoNextImageButton);
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(toy.getName());
        TextView userTextView = view.findViewById(R.id.userTextView);
        User toyUser = dbcom.getUserById(toy.getUserId());
        userTextView.setText(toyUser.getName());
        userTextView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UserPageActivity.class);
            editor.putInt("current_toy_user", toy.getUserId());
            editor.apply();
            v.getContext().startActivity(intent);
        });
        TextView toyAddressTextView = view.findViewById(R.id.toyAddressTextView);
//        Address addressToy = dbcom.getAddressById(toy.getAddressId());
//        toyAddressTextView.setText(addressToy.getAddress());
        TextView conditionValueTextView = view.findViewById(R.id.conditionValueTextView);
        conditionValueTextView.setText(toy.getCondition());
        TextView categoryValueTextView = view.findViewById(R.id.categoryValueTextView);
        categoryValueTextView.setText(toy.getCategory());
        TextView ageValueTextView = view.findViewById(R.id.ageValueTextView);
        ageValueTextView.setText(toy.getAgeCategory());
        TextView descriptionValueTextView = view.findViewById(R.id.descriptionValueTextView);
        descriptionValueTextView.setText(toy.getDescription());
        TextView preferencesValueTextView = view.findViewById(R.id.preferencesValueTextView);
        preferencesValueTextView.setText(toy.getExchangePreferences());

        Button toyPageButton = view.findViewById(R.id.toyPageButton);
//        toyAddressTextView.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), MapActivity.class);
//            v.getContext().startActivity(intent);
//        });
        Bundle args = getArguments();
        if (args != null) {
            String source = args.getString("source");
            if ("fromMainPage".equals(source)) {
                toyPageButton.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), AskForChangeActivity.class);
                    v.getContext().startActivity(intent);
                });
            } else if ("fromActiveToys".equals(source)) {
                toyPageButton.setText("Убрать в архив");
                toyPageButton.setOnClickListener(v -> {
                    dbcom.updateToyActivity(currentToyId, false);
                    if (listener != null) {
                        listener.onButtonClicked();
                    }
                });
            } else if ("fromArchiveToys".equals(source)) {
                toyPageButton.setText("Достать из архива");
                toyPageButton.setOnClickListener(v -> {
                    dbcom.updateToyActivity(currentToyId, true);
                    if (listener != null) {
                        listener.onButtonClicked();
                    }
                });
            }
        }

        return view;
    }
}

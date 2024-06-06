package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends Fragment {
    private DBCommands dbcom;
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        dbcom = new DBCommands(view.getContext());

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int blueColor = resources.getColor(R.color.blue);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        User user = dbcom.getUserById(currentUserId);
        Button addAddressButton = view.findViewById(R.id.addAddressButton);
        Button profileSaveButton = view.findViewById(R.id.profileSaveButton);
        EditText editNameValueEditText = view.findViewById(R.id.editNameValueEditText);
        editNameValueEditText.setText(user.getName());
        EditText editEmailValueEditText = view.findViewById(R.id.editEmailValueEditText);
        editEmailValueEditText.setText(user.getEmail());
        EditText editPhoneValueEditText = view.findViewById(R.id.editPhoneValueEditText);
        editPhoneValueEditText.setText(user.getPhone());
        LinearLayout userEditAddressesLinearLayout = view.findViewById(R.id.userEditAddressesLinearLayout);
        List<Integer> addressesIds = dbcom.getAllUserAddresses(currentUserId);
        if (!addressesIds.isEmpty()) {
            for (int addressId : addressesIds) {
                Address address = dbcom.getAddressById(addressId);
                LinearLayout linearLayout = new LinearLayout(view.getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(8, 8, 8, 8);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.weight = 3;
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                layoutParams1.weight = 1;
                linearLayout.setLayoutParams(layoutParams);
                TextView addressTextView = new TextView(view.getContext());
                addressTextView.setPadding(8, 8, 8, 8);
                addressTextView.setTextSize(16);
                addressTextView.setLayoutParams(layoutParams1);
                addressTextView.setText(address.getAddress());
                ImageButton deleteAddressButton = new ImageButton(view.getContext());
                deleteAddressButton.setImageResource(R.drawable.delete);
                deleteAddressButton.setLayoutParams(layoutParams);
                deleteAddressButton.setOnClickListener(v -> {
                    dbcom.deleteAddress(addressId);
                    userEditAddressesLinearLayout.removeView((View) v.getParent());
                });
                linearLayout.addView(addressTextView);
                linearLayout.addView(deleteAddressButton);
                userEditAddressesLinearLayout.addView(linearLayout);
            }
        }
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapActivity.class);
            v.getContext().startActivity(intent);
        });
        profileSaveButton.setOnClickListener(v -> {
            String name = editNameValueEditText.getText().toString();
            String email = editEmailValueEditText.getText().toString();
            String phone = editPhoneValueEditText.getText().toString();
            User newUser = new User(name, email, phone, user.getPassword(), user.getPhoto());
            dbcom.updateUser(newUser, currentUserId);
            if (listener != null) {
                listener.onButtonClicked();
            }
        });
        return view;
    }
}

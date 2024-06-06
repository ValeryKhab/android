package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MyProfileFragment extends Fragment {
    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        dbcom = new DBCommands(view.getContext());

        Context context = getContext();
        assert context != null;
        Resources resources = context.getResources();
        int blueColor = resources.getColor(R.color.blue);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        Button exitButton = view.findViewById(R.id.exitButton);
        ImageButton editProfileButton = view.findViewById(R.id.editProfileImageButton);
        LinearLayout userAddressesLinearLayout = view.findViewById(R.id.userAddressesLinearLayout);
        User user = dbcom.getUserById(currentUserId);
        List<Integer> addressesIds = dbcom.getAllUserAddresses(currentUserId);
        if (addressesIds.isEmpty()) {
            TextView addressTextView = new TextView(view.getContext());
            userAddressesLinearLayout.addView(addressTextView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            addressTextView.setLayoutParams(layoutParams);
            addressTextView.setText("У вас нет адресов");
        } else {
            for (int addressId : addressesIds) {
                Address address = dbcom.getAddressById(addressId);
                TextView addressTextView = new TextView(view.getContext());
                userAddressesLinearLayout.addView(addressTextView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                addressTextView.setLayoutParams(layoutParams);
                addressTextView.setText(address.getAddress());
            }
        }
        TextView profileUserNameValueTextView = view.findViewById(R.id.profileUserNameValueTextView);
        profileUserNameValueTextView.setText(user.getName());
        TextView profileEmailValueTextView = view.findViewById(R.id.profileEmailValueTextView);
        profileEmailValueTextView.setText(user.getEmail());
        TextView profilePhoneValueTextView = view.findViewById(R.id.profilePhoneValueTextView);
        profilePhoneValueTextView.setText(user.getPhone());
        if (user.getPhoto() != null) {
            ImageView profileImageView = view.findViewById(R.id.profileImageView);
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
            profileImageView.setImageBitmap(bitmap);
        }
        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LogInActivity.class);
            v.getContext().startActivity(intent);
        });
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditProfileActivity.class);
            v.getContext().startActivity(intent);
        });
        return view;
    }
}

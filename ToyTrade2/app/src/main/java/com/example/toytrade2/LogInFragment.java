package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class LogInFragment extends Fragment {
    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        dbcom = new DBCommands(view.getContext());

        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            SignInFragment signInFragment = new SignInFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.log_in_frame, signInFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        EditText editTextLogin = view.findViewById(R.id.editTextLogin);
        EditText editTextTextPassword = view.findViewById(R.id.editTextTextPassword);
        Button mainLogInButton = view.findViewById(R.id.mainLogInButton);
        mainLogInButton.setOnClickListener(v -> {
            String phone = editTextLogin.getText().toString();
            int userId = dbcom.findUserByPhoneNumber(phone);
            String password = editTextTextPassword.getText().toString();
            if (userId != -1) {
                User user = dbcom.getUserById(userId);
                if (password.equals(user.getPassword())) {
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("current_user_id", userId);
                    editor.apply();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }
}

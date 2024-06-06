package com.example.toytrade2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SignInFragment extends Fragment {
    private DBCommands dbcom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        dbcom = new DBCommands(view.getContext());

        Button logInButton = view.findViewById(R.id.logInButton);
        logInButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                LogInFragment logInFragment = new LogInFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.log_in_frame, logInFragment);
                transaction.commit();
            }
        });

        Button mainSignInButton = view.findViewById(R.id.mainSignInButton);
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        EditText editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        EditText editTextRepPassword = view.findViewById(R.id.editTextRepPassword);
        mainSignInButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String phone = editTextPhone.getText().toString();
            String password = editTextNewPassword.getText().toString();
            String repPassword = editTextRepPassword.getText().toString();
            if (password.equals(repPassword)) {
                User user = new User(name, email, phone, password, null);
                dbcom.addUser(user);
                Intent intent = new Intent(v.getContext(), LogInActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}

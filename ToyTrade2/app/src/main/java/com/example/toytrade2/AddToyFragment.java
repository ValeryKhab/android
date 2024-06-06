package com.example.toytrade2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class AddToyFragment extends Fragment {

    private DBCommands dbcom;

    String[] conditions = {"Б/у", "Новое"};
    String[] categories = {"Мягкие игрушки", "Развивающие игрушки", "Машинки", "Куклы", "Фигурки",
            "Конструкторы", "Настольные игры", "Головоломки", "Музыкальные инструменты", "Оружие",
            "Бижутерия", "Косметика", "Спорт", "Одежда", "Книги", "Для творчества", "Другое"};
    String[] ages = {"Нет", "0+", "3+", "6+", "12+", "16+"};
    public interface OnButtonClickListener {
        void onButtonClicked();
    }

    private OnButtonClickListener listener;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            listener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context + "must implement OnButtonClickListener");
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
        View view = inflater.inflate(R.layout.fragment_edit_toy, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("current_user_id", -1);
        User user = dbcom.getUserById(currentUserId);
        List<Integer> userAddressesId = dbcom.getAllUserAddresses(currentUserId);
        ArrayList<String> userAddresses = new ArrayList<>();
        for (Integer addressId : userAddressesId) {
            userAddresses.add(dbcom.getAddressById(addressId).getAddress());
        }

        Button addAddressButton = view.findViewById(R.id.addAddressButton);
        addAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapActivity.class);
            v.getContext().startActivity(intent);
        });
        EditText toyTitleValueEditText = view.findViewById(R.id.toyTitleValueEditText);
        ImageButton newToyPhotoImageButton = view.findViewById(R.id.newToyPhotoImageButton);
        Spinner conditionSpinner = view.findViewById(R.id.conditionSpinner);
        ArrayAdapter<String> cond_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, conditions);
        cond_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(cond_adapter);
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> cat_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, categories);
        cat_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(cat_adapter);
        Spinner ageSpinner = view.findViewById(R.id.ageSpinner);
        ArrayAdapter<String> age_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ages);
        age_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(age_adapter);
        EditText toyDescriptionValueEditText = view.findViewById(R.id.toyDescriptionValueEditText);
        EditText toyPreferencesValueEditText = view.findViewById(R.id.toyPreferencesValueEditText);
        Spinner addressSpinner = view.findViewById(R.id.addressSpinner);
        ArrayAdapter<String> address_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, userAddresses);
        address_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(address_adapter);
        Button addButton = view.findViewById(R.id.toySaveButton);

        Bundle args = getArguments();

        if (args != null) {
            String source = args.getString("source");
            if ("editToy".equals(source)) {
                int currentToyId = sharedPreferences.getInt("current_toy_id", -1);
                Toy toy = dbcom.getToyById(currentToyId);
                toyTitleValueEditText.setText(toy.getName());
                conditionSpinner.setSelection(((ArrayAdapter<String>) conditionSpinner.getAdapter()).getPosition(toy.getCondition()));
                categorySpinner.setSelection(((ArrayAdapter<String>) categorySpinner.getAdapter()).getPosition(toy.getCategory()));
                ageSpinner.setSelection(((ArrayAdapter<String>) ageSpinner.getAdapter()).getPosition(toy.getAgeCategory()));
                toyDescriptionValueEditText.setText(toy.getDescription());
                toyPreferencesValueEditText.setText(toy.getExchangePreferences());
                Address address = dbcom.getAddressById(toy.getAddressId());
                if (address != null) {
                    addressSpinner.setSelection(((ArrayAdapter<String>) addressSpinner.getAdapter()).getPosition(address.getAddress()));
                }
                addButton.setOnClickListener(v -> {
                    int toyAddressId = toy.getAddressId();
                    String toyName = toyTitleValueEditText.getText().toString();
                    String toyCondition = conditionSpinner.getSelectedItem().toString();
                    String toyCategory = categorySpinner.getSelectedItem().toString();
                    String toyAge = ageSpinner.getSelectedItem().toString();
                    String toyDescription = toyDescriptionValueEditText.getText().toString();
                    String toyPreferences = toyPreferencesValueEditText.getText().toString();
                    if (addressSpinner.getSelectedItem() != null) {
                        String toyAddressStr = addressSpinner.getSelectedItem().toString();
                        toyAddressId = dbcom.getToyAddressId(toyAddressStr, currentUserId);
                    }
                    Toy editedToy = new Toy(currentUserId, toyName, toyCondition,
                            toyCategory, toyAge, toyDescription, toyPreferences, toyAddressId, true);
                    dbcom.updateToy(editedToy, currentToyId, new ArrayList<>(), new ArrayList<>());
                    if (listener != null) {
                        listener.onButtonClicked();
                    }
                });
            } else if ("addToy".equals(source)) {
                addButton.setOnClickListener(v -> {
                    String toyName = toyTitleValueEditText.getText().toString();
                    String toyCondition = conditionSpinner.getSelectedItem().toString();
                    String toyCategory = categorySpinner.getSelectedItem().toString();
                    String toyAge = ageSpinner.getSelectedItem().toString();
                    String toyDescription = toyDescriptionValueEditText.getText().toString();
                    String toyPreferences = toyPreferencesValueEditText.getText().toString();
                    String toyAddressStr = addressSpinner.getSelectedItem().toString();
                    int toyAddressId = dbcom.getToyAddressId(toyAddressStr, currentUserId);
                    Toy newToy = new Toy(currentUserId, toyName, toyCondition,
                            toyCategory, toyAge, toyDescription, toyPreferences, toyAddressId, true);
                    dbcom.addToy(newToy, new ArrayList<>());
                    if (listener != null) {
                        listener.onButtonClicked();
                    }
                });
            }
        }

        return view;
    }
}

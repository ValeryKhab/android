package com.example.toytrade2;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapType;
import com.yandex.mapkit.mapview.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment {

    private DBCommands dbcom;
    private EditText mapEditText;
    private MapView mapView;
    private String address;

    public interface OnButtonClickListener {
        void onButtonClicked();
    }

    private MapFragment.OnButtonClickListener listener;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MapFragment.OnButtonClickListener) {
            listener = (MapFragment.OnButtonClickListener) context;
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
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY);
        MapKitFactory.initialize(this.requireContext());
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        dbcom = new DBCommands(view.getContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

        int currentUserId = sharedPreferences.getInt("current_user_id", -1);

        mapView = view.findViewById(R.id.mapView);
        mapEditText = view.findViewById(R.id.mapEditTextView);
        Button mapButton = view.findViewById(R.id.mapButton);
        mapView.getMap().setMapType(MapType.MAP);
        mapView.getMap().move(new CameraPosition(new Point(55.751225, 37.629540), 16, 0, 0));
        InputListener inputListener = new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
                mapObjects.clear();
                mapObjects.addPlacemark(point);
                Geocoder geocoder = new Geocoder(view.getContext());
                try {
                    List <android.location.Address> addresses = geocoder.getFromLocation(point.getLatitude(), point.getLongitude(), 1);
                    if (addresses != null) {
                        android.location.Address returnedAddress = addresses.get(0);
                        address = returnedAddress.getAddressLine(0);
                        mapEditText.setText(address);
                    } else {
                        address = "Error";
                        mapEditText.setText(address);
                    }
                } catch (IOException e) {
                    address = "Error";
                    mapEditText.setText(address);
                }
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {
            }
        };
        mapView.getMap().addInputListener(inputListener);

        mapButton.setOnClickListener(v -> {
            String addressStr = mapEditText.getText().toString();
            Address address = new Address(currentUserId, addressStr);
            dbcom.addAddress(address);
            if (listener != null) {
                listener.onButtonClicked();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}
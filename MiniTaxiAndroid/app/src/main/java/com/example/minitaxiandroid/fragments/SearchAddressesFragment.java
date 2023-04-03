package com.example.minitaxiandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.minitaxiandroid.R;
import com.example.minitaxiandroid.activities.MainActivity;
import com.example.minitaxiandroid.activities.MakeOrderActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SearchAddressesFragment extends Fragment {

    private ListView favouriteAddressesListView;
    private TextInputEditText searchFromTextInputEditText;
    private TextInputLayout searchFromTextInputLayout;
    private TextInputEditText searchToTextInputEditText;
    private TextInputLayout searchToTextInputLayout;
    private ImageButton favouriteAddressesFromButton;
    private ImageButton favouriteAddressesToButton;
    private ImageButton searchAddressToButton;
    private ImageButton searchAddressFromButton;
    private ArrayAdapter<String> favouriteAddressesAdapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private MainActivity activity;
    private Button orderMainMenuButton;
    private List<Place.Field> fields;
    private Place placeFrom;
    private Place placeTo;
    private ActivityResultLauncher<Intent> activityResultLauncherFrom;
    private ActivityResultLauncher<Intent> activityResultLauncherTo;
    private int driverId = 0;
    private boolean isSearchFrom = false;
    private boolean isCloseFrom = false;
    private boolean isCloseTo = false;
    private boolean isPlaceEnable = false;

    @SuppressWarnings("DuplicatedCode")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_addresses,
                container, false);
        activity = (MainActivity) getActivity();
        String message = "";
        String otherAddress = "";
        if (getArguments()!=null) {
            message = getArguments().getString("messageTo");
            otherAddress = getArguments().getString("otherAddress");
        }
        searchFromTextInputEditText = view.findViewById(R.id.searchFromTextInputEditText);
        searchFromTextInputLayout = view.findViewById(R.id.searchFromTextInputLayout);
        searchToTextInputEditText = view.findViewById(R.id.searchToTextInputEditText);
        searchToTextInputLayout = view.findViewById(R.id.searchToTextInputLayout);
        if(!Places.isInitialized()){
            Places.initialize(this.getContext(), getResources().getString(R.string.google_maps_key));
        }
        PlacesClient placesClient = Places.createClient(this.getContext());
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        activityResultLauncherFrom = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result != null && result.getResultCode() == RESULT_OK){
                       if(result.getData() != null){
                            placeFrom = Autocomplete.getPlaceFromIntent(result.getData());
                            searchFromTextInputEditText.setText(placeFrom.getName());
                            searchFromTextInputLayout.getEditText().setCursorVisible(false);
                            searchFromTextInputLayout.getEditText().setTextIsSelectable(false);
                            searchFromTextInputLayout.getEditText().setFocusableInTouchMode(false);
                            searchFromTextInputLayout.getEditText().clearFocus();
                       }
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        isCloseFrom = false;
                        searchFromTextInputLayout.getEditText().clearFocus();
                    }
                }
        );
        activityResultLauncherTo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result != null && result.getResultCode() == RESULT_OK){
                        if(result.getData() != null){
                            placeTo = Autocomplete.getPlaceFromIntent(result.getData());
                            searchToTextInputEditText.setText(placeTo.getName());
                            searchToTextInputLayout.getEditText().setCursorVisible(false);
                            searchToTextInputLayout.getEditText().setTextIsSelectable(false);
                            searchToTextInputLayout.getEditText().setFocusableInTouchMode(false);
                            searchToTextInputLayout.getEditText().clearFocus();
                        }
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        isCloseTo = false;
                        searchToTextInputLayout.getEditText().clearFocus();
                    }
                }
        );
        searchFromTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(!isCloseFrom){
                        Intent intent = new Autocomplete.IntentBuilder(
                                AutocompleteActivityMode.FULLSCREEN, fields)
                                .setCountry("UA")
                                .setLocationRestriction(RectangularBounds.newInstance(
                                        new LatLng(50.1698, 26.1859),
                                        new LatLng(50.6233, 27.2049)))
                                .build(SearchAddressesFragment.this.getContext());
                        if(isPlaceEnable) {
                            activityResultLauncherFrom.launch(intent);
                        }
                        isCloseFrom = true;
                    }
                }
            }
        });
        searchFromTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TextInputLayoutFrom", "TextInputLayoutFrom closed");
                isCloseFrom = false;
                searchFromTextInputLayout.getEditText().setText("");
                searchFromTextInputLayout.getEditText().setFocusableInTouchMode(true);
                searchFromTextInputLayout.getEditText().clearFocus();
            }
        });
        searchToTextInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b && !isCloseTo) {
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .setCountry("UA")
                            .setLocationRestriction(RectangularBounds.newInstance(
                                    new LatLng(50.1698, 26.1859),
                                    new LatLng(50.6233, 27.2049)))
                            .build(SearchAddressesFragment.this.getContext());
                    if(isPlaceEnable) {
                        activityResultLauncherTo.launch(intent);
                    }
                    isCloseTo = true;
                }
            }
        });
        searchToTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TextInputLayoutTo", "TextInputLayoutTo closed");
                searchToTextInputLayout.getEditText().setText("");
                searchToTextInputLayout.getEditText().clearFocus();
                isCloseTo = false;
            }
        });
        orderMainMenuButton = view.findViewById(R.id.orderMainMenuButton);
        orderMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
            }
        });
        if (Objects.equals(message, "From")){
            searchFromTextInputEditText.setText(getArguments().getString("address"));
            searchToTextInputEditText.setText(otherAddress);
        }
        else if (Objects.equals(message, "To")){
            searchToTextInputEditText.setText(getArguments().getString("address"));
            searchFromTextInputEditText.setText(otherAddress);
        }
        searchAddressFromButton = view.findViewById(R.id.mapSearchFromImageButton);
        searchAddressToButton = view.findViewById(R.id.mapSearchToImageButton);
        searchAddressFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddressFromMap("From");
            }
        });
        searchAddressToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddressFromMap("To");
            }
        });
        favouriteAddressesFromButton = view.findViewById(R.id.favouriteAddressesFromImageButton);
        favouriteAddressesFromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearchFrom = true;
                createFavouriteAddressesListSearchDialog();
            }
        });
        favouriteAddressesToButton = view.findViewById(R.id.favouriteAddressesToImageButton);
        favouriteAddressesToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearchFrom = false;
                createFavouriteAddressesListSearchDialog();
            }
        });
        return view;
    }

    private void order() {
        if(placeFrom == null){
            Toast.makeText(SearchAddressesFragment.this.getContext(),
                    getResources().getString(R.string.please_select_address_from_toast_text),
                    Toast.LENGTH_SHORT).show();
        }
        else if(placeTo == null){
            Toast.makeText(SearchAddressesFragment.this.getContext(),
                    getResources().getString(R.string.please_select_address_to_toast_text),
                    Toast.LENGTH_SHORT).show();
        }
        else{
            if(driverId > 0){
                //TODO
            }
            else {
                Intent intent = new Intent(SearchAddressesFragment.this.getContext(), MakeOrderActivity.class);
                intent.putExtra("userAddressFrom", placeFrom.getAddress());
                intent.putExtra("userAddressTo",  placeTo.getAddress());
                startActivity(intent);
            }
        }
    }

    private void getAddressFromMap(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("messageFrom", message);
        if(!searchFromTextInputEditText.getText().toString().equals("")){
            bundle.putString("otherAddress", searchFromTextInputEditText.getText().toString());
        }
        else if(!searchToTextInputEditText.getText().toString().equals("")){
            bundle.putString("otherAddress", searchToTextInputEditText.getText().toString());
        }
        SearchAddressOnMapFragment searchAddressOnMapFragment = new SearchAddressOnMapFragment();
        searchAddressOnMapFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, searchAddressOnMapFragment);
        fragmentTransaction.commit();
    }

    public void createFavouriteAddressesListSearchDialog(){
        dialogBuilder = new AlertDialog.Builder(SearchAddressesFragment.this.getContext());
        final View contactPopUpView = getLayoutInflater().inflate(R.layout.favourite_addresses_search_fragment, null);
        favouriteAddressesListView = contactPopUpView.findViewById(R.id.favouriteAddressesSearchList);
        dialogBuilder.setView(contactPopUpView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundcorner);
        dialog.show();
        setFavouriteAddresses();
    }

    private void setFavouriteAddresses() {
        favouriteAddressesAdapter = new ArrayAdapter<>(SearchAddressesFragment.this.getContext(),
                android.R.layout.simple_list_item_1, activity.getFavouriteAddressesList());
        favouriteAddressesListView.setAdapter(favouriteAddressesAdapter);
        favouriteAddressesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String address = String.valueOf(adapterView.getAdapter().getItem(i));
                if (isSearchFrom){
                    searchFromTextInputEditText.setText(address);
                }
                else {
                    searchToTextInputEditText.setText(address);
                }
                dialog.dismiss();
            }
        });
    }

}
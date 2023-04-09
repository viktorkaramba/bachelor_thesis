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
import com.example.minitaxiandroid.constants.DistanceConstants;
import com.example.minitaxiandroid.entities.userinfo.DriverInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
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
    private TextInputEditText favouriteDriverTextInputEditText;
    private TextInputLayout  favouriteDriverTextInputLayout;
    private ImageButton favouriteAddressesFromButton, favouriteAddressesToButton, searchAddressToButton,
            searchAddressFromButton;
    private ArrayAdapter<String> favouriteAddressesAdapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private MainActivity activity;
    private Button orderMainMenuButton;
    private List<Place.Field> fields;
    private Place placeFrom, placeTo;
    private ActivityResultLauncher<Intent> activityResultLauncherFrom, activityResultLauncherTo;
    private int driverId = 0;
    private float distance = 0;
    private boolean isSearchFrom, isCloseFrom, isCloseTo = false;
    private boolean isPlaceEnable = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_addresses,
                container, false);
        activity = (MainActivity) getActivity();
        activity.setFragment();
        String message = "";
        String otherAddress = "";
        String favouriteDriver = "";
        if (getArguments()!=null) {
            message = getArguments().getString("messageTo");
            otherAddress = getArguments().getString("otherAddress");
            favouriteDriver = getArguments().getString("favouriteDriver");
        }
        searchFromTextInputEditText = view.findViewById(R.id.searchFromTextInputEditText);
        searchFromTextInputLayout = view.findViewById(R.id.searchFromTextInputLayout);
        searchToTextInputEditText = view.findViewById(R.id.searchToTextInputEditText);
        searchToTextInputLayout = view.findViewById(R.id.searchToTextInputLayout);
        favouriteDriverTextInputLayout = view.findViewById(R.id.favouriteDriverTextInputLayout);
        favouriteDriverTextInputEditText = view.findViewById(R.id.favouriteDriverTextInputEditText);
        if(!Places.isInitialized()){
            Places.initialize(this.getContext(), getResources().getString(R.string.google_maps_key));
        }
        PlacesClient placesClient = Places.createClient(this.getContext());
        fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS, Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);
        activityResultLauncherFrom = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result != null && result.getResultCode() == RESULT_OK){
                       if(result.getData() != null){
                            placeFrom = Autocomplete.getPlaceFromIntent(result.getData());
                            searchFromTextInputEditText.setText(placeFrom.getName());
                            setTextEditUntouchable(searchFromTextInputLayout);
                            setCustomEndIconForSearchFrom();
                       }
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        isCloseFrom = false;
                        Objects.requireNonNull(searchFromTextInputLayout.getEditText()).clearFocus();
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
                            setTextEditUntouchable(searchToTextInputLayout);
                            setCustomEndIconForSearchTo();
                        }
                    }
                    else if(result.getResultCode() == RESULT_CANCELED){
                        isCloseTo = false;
                        Objects.requireNonNull(searchToTextInputLayout.getEditText()).clearFocus();
                    }
                }
        );
        setOnClickSearchForListener();
        setOnClickSearchToListener();
        orderMainMenuButton = view.findViewById(R.id.orderMainMenuButton);
        orderMainMenuButton.setOnClickListener(view16 -> order());
        if(!Objects.equals(favouriteDriver, "")){
            favouriteDriverTextInputEditText.setText(favouriteDriver);
            favouriteDriverTextInputEditText.setTextSize(20);
            setTextEditUntouchable(favouriteDriverTextInputLayout);
            setCustomEndIconForFavouriteDriver(favouriteDriverTextInputLayout, favouriteDriverTextInputEditText);
        }
        if (Objects.equals(message, "From")){
            searchFromTextInputEditText.setText(getArguments().getString("address"));
            setTextEditUntouchable(searchFromTextInputLayout);
            setCustomEndIconForSearchFrom();
            if(!otherAddress.equals("")){
                searchToTextInputEditText.setText(otherAddress);
                setTextEditUntouchable(searchToTextInputLayout);
                setCustomEndIconForSearchTo();
            }
        }
        else if (Objects.equals(message, "To")){
            searchToTextInputEditText.setText(getArguments().getString("address"));
            setTextEditUntouchable(searchToTextInputLayout);
            setCustomEndIconForSearchTo();
            if(!otherAddress.equals("")){
                searchFromTextInputEditText.setText(otherAddress);
                setTextEditUntouchable(searchFromTextInputLayout);
                setCustomEndIconForSearchFrom();
            }
        }
        searchAddressFromButton = view.findViewById(R.id.mapSearchFromImageButton);
        searchAddressToButton = view.findViewById(R.id.mapSearchToImageButton);
        searchAddressFromButton.setOnClickListener(view17 -> getAddressFromMap("From"));
        searchAddressToButton.setOnClickListener(view18 -> getAddressFromMap("To"));
        favouriteAddressesFromButton = view.findViewById(R.id.favouriteAddressesFromImageButton);
        favouriteAddressesFromButton.setOnClickListener(view19 -> {
            isSearchFrom = true;
            createFavouriteAddressesListSearchDialog();
        });
        favouriteAddressesToButton = view.findViewById(R.id.favouriteAddressesToImageButton);
        favouriteAddressesToButton.setOnClickListener(view110 -> {
            isSearchFrom = false;
            createFavouriteAddressesListSearchDialog();
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
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                            placeFrom.getAddress() + "&destinations=" + placeTo.getAddress() + "&key=" + getResources().getString(R.string.google_maps_key))
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(SearchAddressesFragment.this.getContext(),
                            getResources().getString(R.string.error_to_get_distance),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        try {
                           distance = Float.parseFloat(convertJson(response.body().string()))/1000;
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(distance < DistanceConstants.MIN_DISTANCE){
                            Toast.makeText(SearchAddressesFragment.this.getContext(),
                                    getResources().getString(R.string.distance_is_to_short),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(distance > DistanceConstants.MAX_DISTANCE){
                            Toast.makeText(SearchAddressesFragment.this.getContext(),
                                    getResources().getString(R.string.distance_is_to_long),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            goToMakeOrder();
                        }
                    }
                    else {
                        Toast.makeText(SearchAddressesFragment.this.getContext(),
                                getResources().getString(R.string.error_to_get_distance),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void goToMakeOrder(){
        Intent intent = new Intent(SearchAddressesFragment.this.getContext(), MakeOrderActivity.class);
        intent.putExtra("userAddressFrom", placeFrom.getAddress());
        intent.putExtra("userAddressTo",  placeTo.getAddress());
        intent.putExtra("latitude",  placeFrom.getLatLng().latitude);
        intent.putExtra("longitude",  placeFrom.getLatLng().longitude);
        intent.putExtra("driverId",  driverId);
        intent.putExtra("distance",  distance);
        startActivity(intent);
    }

    private void getAddressFromMap(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("messageFrom", message);
        if(driverId != 0){
            bundle.putString("favouriteDriver", favouriteDriverTextInputEditText.getText().toString());
        }
        else{
            bundle.putString("favouriteDriver", "");
        }
        if(message.equals("From")){
            if(!searchToTextInputEditText.getText().toString().equals("")){
                bundle.putString("otherAddress", searchToTextInputEditText.getText().toString());
            }
            else {
                bundle.putString("otherAddress", "");
            }
        }
        else{
            if(!searchFromTextInputEditText.getText().toString().equals("")){
                bundle.putString("otherAddress", searchFromTextInputEditText.getText().toString());
            }
            else{
                bundle.putString("otherAddress", "");
            }
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
        favouriteAddressesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String address = String.valueOf(adapterView.getAdapter().getItem(i));
            if (isSearchFrom){
                searchFromTextInputEditText.setText(address);
                setTextEditUntouchable(searchFromTextInputLayout);
                setCustomEndIconForSearchFrom();
            }
            else {
                searchToTextInputEditText.setText(address);
                setTextEditUntouchable(searchToTextInputLayout);
                setCustomEndIconForSearchTo();
            }
            dialog.dismiss();
        });
    }

    public void setFavouriteDriver(DriverInfo driverInfo){
        String name = driverInfo.getDriverFirstName() + " " + driverInfo.getDriverSurName();
        driverId = driverInfo.getDriverId();
        favouriteDriverTextInputEditText.setText(name);
        favouriteDriverTextInputLayout.setEndIconVisible(true);
        favouriteDriverTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        favouriteDriverTextInputEditText.setTextSize(20);
        setCustomEndIconForFavouriteDriver(favouriteDriverTextInputLayout, favouriteDriverTextInputEditText);
    }

    public void setAddressUserInSearchForm(String address){
        searchFromTextInputEditText.setText(address);
        setTextEditUntouchable(searchFromTextInputLayout);
        setCustomEndIconForSearchFrom();
    }

    private void setTextEditUntouchable(TextInputLayout textInputLayout){
        textInputLayout.setEndIconVisible(true);
        textInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        textInputLayout.getEditText().setCursorVisible(false);
        textInputLayout.getEditText().setTextIsSelectable(false);
        textInputLayout.getEditText().setFocusableInTouchMode(false);
        textInputLayout.getEditText().clearFocus();
    }

    private void setCustomEndIconForFavouriteDriver(TextInputLayout textInputLayout, TextInputEditText textInputEditText){
        textInputLayout.setEndIconOnClickListener(view -> {
            Log.d("TextInputLayoutTo", "TextInputLayoutTo closed");
            textInputLayout.getEditText().setText(getResources().getString(R.string.pick_favourite_driver_text));
            textInputLayout.getEditText().clearFocus();
            textInputLayout.setEndIconVisible(false);
            textInputEditText.setTextSize(14);
            driverId = 0;
        });
    }

    private void setCustomEndIconForSearchFrom(){
        searchFromTextInputLayout.setEndIconOnClickListener(view -> {
            isCloseFrom = false;
            searchFromTextInputLayout.getEditText().setText("");
            searchFromTextInputLayout.getEditText().setFocusableInTouchMode(true);
            searchFromTextInputLayout.getEditText().setTextIsSelectable(true);
            searchFromTextInputLayout.getEditText().clearFocus();
            searchFromTextInputLayout.setEndIconVisible(false);
        });
    }

    private void setCustomEndIconForSearchTo(){
        searchToTextInputLayout.setEndIconOnClickListener(view -> {
            isCloseTo = false;
            searchToTextInputLayout.getEditText().setText("");
            searchToTextInputLayout.getEditText().setFocusableInTouchMode(true);
            searchToTextInputLayout.getEditText().setTextIsSelectable(true);
            searchToTextInputLayout.getEditText().clearFocus();
            searchToTextInputLayout.setEndIconVisible(false);
        });
    }

    private void setOnClickSearchForListener(){
        searchFromTextInputLayout.getEditText().setOnFocusChangeListener((view13, b) -> {
            if(b) {
                if(!isCloseFrom){
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .setLocationRestriction(RectangularBounds.newInstance(
                                    new LatLng(50.1698, 26.1859),
                                    new LatLng(50.6233, 27.2049)))
                            .setCountry("UA")
                            .setTypesFilter(new ArrayList<String>() {{
                                add(TypeFilter.ADDRESS.toString().toLowerCase());
                            }})
                            .build(SearchAddressesFragment.this.getContext());
                    if(isPlaceEnable) {
                        activityResultLauncherFrom.launch(intent);
                        isCloseFrom = true;
                    }
                }
            }
        });
        setCustomEndIconForSearchFrom();
    }

    private void setOnClickSearchToListener(){
        searchToTextInputLayout.getEditText().setOnFocusChangeListener((view13, b) -> {
            if(b) {
                if(!isCloseTo){
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .setLocationRestriction(RectangularBounds.newInstance(
                                    new LatLng(50.1698, 26.1859),
                                    new LatLng(50.6233, 27.2049)))
                            .setCountry("UA")
                            .setTypesFilter(new ArrayList<String>() {{
                                add(TypeFilter.ADDRESS.toString().toLowerCase());
                            }})
                            .build(SearchAddressesFragment.this.getContext());
                    if(isPlaceEnable) {
                        activityResultLauncherTo.launch(intent);
                        isCloseTo = true;
                    }
                }
            }
        });
        setCustomEndIconForSearchTo();
    }

    private String convertJson(String response) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONArray jsonArray = (JSONArray) jsonObject.get("rows");
        jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray = (JSONArray) jsonObject.get("elements");
        jsonObject = (JSONObject) jsonArray.get(0);
        JSONObject result = (JSONObject) jsonObject.get("distance");
        return result.get("value").toString();
    }
}
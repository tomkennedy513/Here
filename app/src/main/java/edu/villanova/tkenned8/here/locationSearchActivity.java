package edu.villanova.tkenned8.here;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class locationSearchActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    LatLng newLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                newLatLng = place.getLatLng();
                Intent returnLocation = new Intent();
                double latitude = newLatLng.latitude;
                double longitude = newLatLng.longitude;
                returnLocation.putExtra("latitude", latitude);
                returnLocation.putExtra("longitude", longitude);
                setResult(RESULT_OK, returnLocation);
                finish();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
}
}

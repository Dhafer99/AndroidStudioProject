package com.example.mobile.ui.vet_map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile.utils.NetworkUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobile.R;
import com.example.mobile.database.UserEntity;
import com.example.mobile.database.repositories.UserRepository;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class VetMapFragment extends Fragment implements OnMapReadyCallback {

    // Other class variables
    private LatLng currentLocation = new LatLng(36.852156, 10.208111);
    private GoogleMap googleMap;
    private UserRepository userRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vet_map, container, false);

        // Initialize UserRepository
        userRepository = new UserRepository(requireContext());

        // Initialize SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Enable zoom controls and gestures
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        this.googleMap = googleMap;
        loadVetLocations();

        // Retrieve the latitude and longitude passed as arguments
        Bundle args = getArguments();
        if (args != null) {
            double vetLat = args.getDouble("lat");
            double vetLng = args.getDouble("lng");
            LatLng vetLocation = new LatLng(vetLat, vetLng);

            // Draw route to a specific veterinarian location (example: first vet in the list)
            Executors.newSingleThreadExecutor().execute(() -> {
                List<UserEntity> vets = userRepository.getAllUsers();
                if (!vets.isEmpty()) {



                    getDirections(currentLocation, vetLocation);
                }
            });
            // Draw route or marker to vetLocation
          //  getDirections(currentLocation, vetLocation); // Assuming getDirections method is implemented
        }



    }

    private void getDirections(LatLng origin, LatLng destination) {
        // Build the URL for the Directions API request
        String apiKey = "GOOGLE MAPS KEY"; // Replace with your actual API key
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=" + apiKey;

        // Execute AsyncTask to fetch route data
        new FetchDirectionsTask().execute(url);
    }

    private void loadVetLocations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Fetch only veterinarians from the database
            List<UserEntity> vets = userRepository.getAllUsers();

            // Post markers to the map on the main thread
            requireActivity().runOnUiThread(() -> {
                if (vets.isEmpty()) {
                    Log.d("VetMapFragment", "No veterinarian locations found.");
                }

                for (UserEntity vet : vets) {
                    LatLng position = new LatLng(vet.getLatitude(), vet.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title(vet.getName()));
                }

                // After adding all markers
                if (!vets.isEmpty()) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (UserEntity vet : vets) {
                        LatLng position = new LatLng(vet.getLatitude(), vet.getLongitude());
                        builder.include(position);
                    }
                    LatLngBounds bounds = builder.build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Adjust padding as needed
                }

            });
        });
    }


    private class FetchDirectionsTask extends AsyncTask<String, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(String... params) {
            String url = params[0];
            List<LatLng> routePoints = new ArrayList<>();
            try {
                String jsonResponse = NetworkUtils.fetchData(url);
                Log.d("FetchDirectionsTask", "Directions API Response: " + jsonResponse); // Log the response

                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray routes = jsonObject.getJSONArray("routes");

                if (routes.length() > 0) {
                    JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                    JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

                    for (int i = 0; i < steps.length(); i++) {
                        String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");
                        routePoints.addAll(decodePolyline(polyline));
                    }
                } else {
                    Log.d("FetchDirectionsTask", "No routes found in the response.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routePoints;
        }

        @Override
        protected void onPostExecute(List<LatLng> routePoints) {
            if (routePoints.isEmpty()) {
                Log.d("FetchDirectionsTask", "No route points found to display.");
            } else {
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(routePoints)
                        .width(10)
                        .color(Color.BLUE);
                googleMap.addPolyline(polylineOptions);
            }
        }
    }


    // Helper method to decode polyline points
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            polyline.add(new LatLng((lat / 1E5), (lng / 1E5)));
        }
        return polyline;
    }



}

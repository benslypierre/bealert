package com.example.bealert.ui.commuteAlert;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bealert.Main2Activity;
import com.example.bealert.R;
import com.example.bealert.broadcastReceiver.ProximityReceiver;
import com.example.bealert.models.Favourites;
import com.example.bealert.shareViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class CommuteFragment extends Fragment {
    //GoogleMap mMap;
    private LocationManager locationManager;
    Location location;
    LatLng destination;
    Boolean destinationSelected = false;
    MarkerOptions destinationMarker = new MarkerOptions();
    Button navigate;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds

    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;
    private static final String PROX_ALERT_INTENT = "com.example.bealert.ui.commuteAlert";
    shareViewModel shareviewmodel;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        @SuppressLint("MissingPermission")
        //@Override
        public void onMapReady(GoogleMap mMap) {
            //LatLng sydney = new LatLng(-34, 151);
            //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            shareviewmodel = new ViewModelProvider(requireActivity()).get(shareViewModel.class);
            if (checkAndRequestPermissions()) {



                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng dest) {


                        if(destinationSelected){
                            mMap.clear();
                            destination = null;

                        }

                        destinationMarker.position(dest)
                                .snippet("Selected Position")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        mMap.addMarker(destinationMarker);
                        destination = dest;
                        destinationSelected = true;
                        Toast.makeText(getContext(), "Location Selected Successfully", Toast.LENGTH_LONG).show();




                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        LayoutInflater inflater = (LayoutInflater)
                                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.favourites_save, null);

                        // create the popup window
                        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        Button savebutton = popupView.findViewById(R.id.save_button) ;
                        Button cancelbutton = popupView.findViewById(R.id.cancel_button) ;
                        EditText favname = popupView.findViewById(R.id.fav_name);


                        savebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (favname.getText().toString().contains("/")) {
                                    Toast.makeText(getContext(), "Location name cannot contain a \"/\"!", Toast.LENGTH_LONG).show();
                                } else if (favname.getText().toString().isEmpty()) {
                                    Toast.makeText(getContext(), "Location name cannot be empty!", Toast.LENGTH_LONG).show();
                                } else {
                                    Favourites favouriteToAdd = new Favourites(favname.getText().toString(), marker.getPosition());
                                    if (shareviewmodel.favourites.contains(favouriteToAdd)) {
                                        Toast.makeText(getContext(), "Already in favourites!", Toast.LENGTH_LONG).show();
                                    } else {
                                        shareviewmodel.addFavourites(favouriteToAdd, (Main2Activity) requireActivity());
                                        popupWindow.dismiss();
                                    }
                                }
                            }

                        });

                        cancelbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });

                        ColorDrawable bg = new ColorDrawable(0x333333);
                        popupWindow.setBackgroundDrawable(bg);



                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);




                        return false;
                    }
                });



                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);



                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    Toast.makeText(getContext(), "No last known location. Aborting...", Toast.LENGTH_LONG).show();
                } else {
                    LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.setMyLocationEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15));
                }



            }

        }
    };





        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_commute, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }

            navigate = view.findViewById(R.id.navigate);
            navigate.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View view) {

                    if(checkAndRequestPermissions()){
                        if(destinationSelected){
                            addProximityAlert(destination.latitude,destination.longitude);

                        }
                        else
                            Toast.makeText(getContext(), "Click on Map to Select Location", Toast.LENGTH_LONG).show();







                    }
                }
            });


        }

    @SuppressLint("MissingPermission")
    private void addProximityAlert(double latitude, double longitude) {

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        locationManager.addProximityAlert(
                latitude, // the latitude of the central point of the alert region
                longitude, // the longitude of the central point of the alert region
                POINT_RADIUS, // the radius of the central point of the alert region, in meters
                PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
                proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
        );

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        requireActivity().registerReceiver(new ProximityReceiver(), filter);
        Toast.makeText(getContext(),"Alert Added",Toast.LENGTH_SHORT).show();
    }


        //Permission Checking

        public boolean checkAndRequestPermissions() {
            int internet = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.INTERNET);
            int loc = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            int loc2 = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (internet != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.INTERNET);
            }
            if (loc != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (loc2 != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions(listPermissionsNeeded.toArray
                        (new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
            return true;
        }
    }



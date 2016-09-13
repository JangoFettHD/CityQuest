package me.jangofetthd.cityquest;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.luseen.spacenavigation.SpaceOnClickListener;


public class MapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    MapView mMapView;
    private GoogleMap googleMap;
    Marker IGY;
    Marker subway;
    Marker trash;
    Marker home;
    Marker school15;
    Marker bridge;

    LatLng initGeo=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                googleMap.getUiSettings().setMapToolbarEnabled(false);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ((MainActivity) getContext()).requestLocationPermission();
                }
                googleMap.setMyLocationEnabled(true);

                for (me.jangofetthd.cityquest.Models.Marker marker : ApplicationClass.markers) {
                    googleMap.addMarker(marker.toGoogleMarkerOptions());
                }

                /*IGY = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.249991, 104.264174)).title("Путешествие по ИГУ").snippet("Замечательное описание").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                subway = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.248178, 104.268964)).title("Путешествие по Сабвею").snippet("0").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                trash = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.287103, 104.309872)).title("Борьба с бомжами").snippet("Выкиньте мусор, но при этом останьтесь в живых").zIndex(2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                home = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.287943, 104.310949)).title("Home.. Sweet home").snippet("Поспать").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                school15 = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.276300, 104.285048)).title("Выучить стих по литре").snippet("Опять работа?").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                bridge = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.259222, 104.281125)).title("Сжечь мост").snippet("Он сжег мосты, но лодочку оставил...").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));*/

                final Marker[] db = {IGY, subway, home, trash, school15, bridge};

                //Location f = LocationServices.FusedLocationApi.getLastLocation(
                  //      MainActivity.mGoogleApiClient);

                //
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        // Called when a new location is found by the network location provider.
                        //makeUseOfNewLocation(location);
                        //checkNearMarkers(db, location);

                        initGeo = new LatLng(location.getLatitude(), location.getLongitude());
                        /////////
                        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick(final Marker marker) {

                                if (isPosition(marker, location)) {

                                    Log.d("", marker.getTitle());
                                    String button = "Принять задание";
                                    String extra = "";

                                    if (marker.getZIndex() == 2) {
                                        button = "В АТАКУ!";
                                        extra = "\nВам дается 60 секунд, чтобы очистить территорию.";
                                    }


                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle(marker.getTitle())
                                            .setMessage(marker.getSnippet() + extra)
                                            .setCancelable(true)
                                            .setNegativeButton(button,
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            if (marker.getZIndex() == 2) {
                                                                Intent intent = new Intent(getActivity(), bitchGame.class);
                                                                getActivity().startActivity(intent);
                                                            }
                                                            if (marker.getZIndex() == -1) {
                                                                Intent intent = new Intent(getActivity(), subwayGame.class);
                                                                getActivity().startActivity(intent);
                                                            }
                                                            dialog.cancel();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        });
                        /////////
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

// Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                //

                ////////начальный зум
                //////////////@TODO УБРАТЬ, так как дублируется
                CameraPosition cameraPosition;
                if (initGeo!=null){
                    cameraPosition = new CameraPosition.Builder().target(initGeo).zoom(18).build();
                }else{
                    LatLng IRK = new LatLng(52.285230, 104.306678);
                    cameraPosition = new CameraPosition.Builder().target(IRK).zoom(16).build();
                }
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //////////////////////

                /*MainActivity.spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
                    @Override
                    public void onCentreButtonClick() {
                        Toast.makeText(getActivity(), "onCentreButtonClick", Toast.LENGTH_SHORT).show();
                        //начальный зум
                        CameraPosition cameraPosition;
                        if (initGeo!=null){
                            cameraPosition = new CameraPosition.Builder().target(initGeo).zoom(16).build();
                        }else{
                            LatLng IRK = new LatLng(52.285230, 104.306678);
                            cameraPosition = new CameraPosition.Builder().target(IRK).zoom(16).build();
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    @Override
                    public void onItemClick(int itemIndex, String itemName) {
                        Toast.makeText(getActivity(), itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                        switch (itemIndex) {
                            case 1:
                                ProfileFragment profileFragment0 = new ProfileFragment();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment0).commit();
                                break;
                            case 0:
                                ShopFragment shopFragment = new ShopFragment();
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, shopFragment).commit();
                                break;
                        }
                    }
                    @Override
                    public void onItemReselected(int itemIndex, String itemName) {
                    }
                });*/

            }
        });

        return rootView;
    }


    //проверяет - есть ли рядом с нами маркеры из базы данных
    public void checkNearMarkers(Marker[] db, Location location) {
        for (Marker aDb : db) {
            if (isPosition(aDb, location)) {//Toast.makeText(getActivity(), "Вы рядом с "+db[i].getTitle(), Toast.LENGTH_LONG).show();
                Log.i("NEAR_MARKER", "Вы рядом с " + aDb.getTitle());
            }
        }
    }

    //определяет нашу позицию
    public Location getLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //User has previously accepted this permission
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            //Not in api-23, no need to prompt
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    //определяет находитесь ли вы рядом с определенным маркером
    public boolean isPosition(Marker marker, Location location) {
        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng markerLocation = marker.getPosition();
            return ((latitude - 0.0007 < markerLocation.latitude) && (latitude + 0.0007 > markerLocation.latitude)) && ((longitude - 0.0007 < markerLocation.longitude) && (longitude + 0.0007 > markerLocation.longitude));
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onCenterButtonClicked() {
        Toast.makeText(getActivity(), "onCentreButtonClick", Toast.LENGTH_SHORT).show();
        //начальный зум
        CameraPosition cameraPosition;
        if (initGeo!=null){
            cameraPosition = new CameraPosition.Builder().target(initGeo).zoom(18).build();
        }else{
            LatLng IRK = new LatLng(52.285230, 104.306678);
            cameraPosition = new CameraPosition.Builder().target(IRK).zoom(16).build();
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}

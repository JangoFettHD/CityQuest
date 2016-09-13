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


public class MapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    MapView mMapView;
    private GoogleMap googleMap;
    Marker IGY;
    Marker subway;
    Marker trash;
    Marker home;
    Marker school15;
    Marker bridge;


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

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //User has previously accepted this permission
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    //Not in api-23, no need to prompt
                    googleMap.setMyLocationEnabled(true);
                }

                //слежение камеры
                /*googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                        CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

                        googleMap.moveCamera(center);
                        googleMap.animateCamera(zoom);
                    }
                });*/

                // For dropping a marker at a point on the Map

                IGY = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.249991, 104.264174)).title("Путешествие по ИГУ").snippet("Замечательное описание").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                subway = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.248178, 104.268964)).title("Путешествие по Сабвею").snippet("Нужно съесть саб").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                trash = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.287103, 104.309872)).title("Борьба с бомжами").snippet("Выкиньте мусор, но при этом останьтесь в живых").zIndex(2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                home = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.287943, 104.310949)).title("Home.. Sweet home").snippet("Поспать").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                school15 = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.276300, 104.285048)).title("Выучить стих по литре").snippet("Опять работа?").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                bridge = googleMap.addMarker(new MarkerOptions().position(new LatLng(52.259222, 104.281125)).title("Сжечь мост").snippet("Он сжег мосты, но лодочку оставил...").zIndex(1).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                final Marker[] db= {IGY, subway, home, trash, school15, bridge};


                Location f = LocationServices.FusedLocationApi.getLastLocation(
                        MainActivity.mGoogleApiClient);


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        //if (getLocation() != null) {
                        checkNearMarkers(db);
                        //}


                    }
                });
                
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(final Marker marker) {

                        if(isPosition(marker)){

                        Log.d("", marker.getTitle());
                        String button = "Принять задание";
                        String extra="";

                        if (marker.getZIndex()==2){
                            button = "В АТАКУ!";
                            extra="\nВам дается 60 секунд, чтобы очистить территорию.";
                        }



                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(marker.getTitle())
                                .setMessage(marker.getSnippet()+extra)
                                .setCancelable(true)
                                .setNegativeButton(button,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (marker.getZIndex()==2){
                                                    Intent intent = new Intent(getActivity(), bitchGame.class);
                                                    getActivity().startActivity(intent);
                                                }
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }}
                });


                LatLng IRK = new LatLng(52.285230, 104.306678);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(IRK).zoom(20).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }


    //проверяет - есть ли рядом с нами маркеры из базы данных
    public void checkNearMarkers(Marker[] db){
        Location loc = getLocation();
        for (int i=0; i<db.length; i++){
            if (isPosition(db[i], loc)) {//Toast.makeText(getActivity(), "Вы рядом с "+db[i].getTitle(), Toast.LENGTH_LONG).show();
                Log.i("NEAR_MARKER","Вы рядом с "+db[i].getTitle());
            }
        }
    }

    //определяет нашу позицию
    public Location getLocation(){
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
    public boolean isPosition(Marker marker){
        double longitude = getLocation().getLongitude();
        double latitude = getLocation().getLatitude();
        LatLng markerLocation = marker.getPosition();
        if(((latitude-0.0005<markerLocation.latitude)&&(latitude+0.0005>markerLocation.latitude))&&((longitude-0.0005<markerLocation.longitude)&&(longitude+0.0005>markerLocation.longitude))){
            return true;


        } else return false;
    }

    public boolean isPosition(Marker marker, Location location){
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng markerLocation = marker.getPosition();
        if(((latitude-0.0005<markerLocation.latitude)&&(latitude+0.0005>markerLocation.latitude))&&((longitude-0.0005<markerLocation.longitude)&&(longitude+0.0005>markerLocation.longitude))){
            return true;


        } else return false;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //  TODO: Prompt with explanation!

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
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
}

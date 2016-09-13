package me.jangofetthd.cityquest.Models;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by JangoFettHD on 13.09.2016.
 */
public class Marker {
    public int id;
    public String title;
    public String snippet;
    public double lat, lng;
    public int type;
    public int icon;
    public int time;
    public String extra;

    public com.google.android.gms.maps.model.MarkerOptions toGoogleMarkerOptions() {
        BitmapDescriptor bitmapDescriptor;
        switch (icon) {
            case -1: bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW); break;
            case 2:  bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE); break;
            case 1:  bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN); break;
            case 3:  bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED); break;
            default: bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
        return new MarkerOptions().position(new LatLng(lat, lng)).title(title).snippet(snippet)
                .zIndex(type).icon(bitmapDescriptor);
    }
}

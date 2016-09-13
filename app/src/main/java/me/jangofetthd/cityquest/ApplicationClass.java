package me.jangofetthd.cityquest;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import me.jangofetthd.cityquest.Models.Marker;

/**
 * Created by JangoFettHD on 13.09.2016.
 */
public class ApplicationClass extends Application {

    public static List<Marker> markers;

    @Override
    public void onCreate() {
        super.onCreate();
        markers = new ArrayList<>();
    }
}

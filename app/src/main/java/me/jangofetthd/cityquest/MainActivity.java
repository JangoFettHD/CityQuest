package me.jangofetthd.cityquest;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    SpaceNavigationView spaceNavigationView;
    FragmentManager fragmentManager;

    static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Маркет", R.drawable.shopping_cart));
        spaceNavigationView.addSpaceItem(new SpaceItem("Профиль", R.drawable.avatar));

        fragmentManager = getSupportFragmentManager();

        MapFragment profileFragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .setAccountName("jangofetthd@gmail.com")
                .build();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(MainActivity.this, "onCentreButtonClick", Toast.LENGTH_SHORT).show();
                MapFragment profileFragment = new MapFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex) {
                    case 1:
                        ProfileFragment profileFragment0 = new ProfileFragment();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, profileFragment0).commit();
                        break;
                    case 0:
                        ShopFragment shopFragment = new ShopFragment();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, shopFragment).commit();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

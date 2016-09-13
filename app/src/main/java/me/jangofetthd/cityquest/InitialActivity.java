package me.jangofetthd.cityquest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import me.jangofetthd.cityquest.Models.Marker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class InitialActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jangofetthd.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CityQuestService service = retrofit.create(CityQuestService.class);
        Call<List<Marker>> markers = service.listMarkers();
        markers.enqueue(new Callback<List<Marker>>() {
            @Override
            public void onResponse(Call<List<Marker>> call, Response<List<Marker>> response) {
                ApplicationClass.markers = response.body();
                Intent goMain = new Intent(context, MainActivity.class);
                startActivity(goMain);
            }

            @Override
            public void onFailure(Call<List<Marker>> call, Throwable t) {
                Log.e("ERROR", t.toString());
                Toast.makeText(InitialActivity.this, "An error occurred during networking. "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*if(isOk()){
            Intent goMain = new Intent(this, MainActivity.class);
            startActivity(goMain);
        }*/
    }

    private boolean isOk(){
        return true;
    }

    public interface CityQuestService {
        @GET("CityQuest/markersdbminify.json")
        Call<List<Marker>> listMarkers();
    }

}

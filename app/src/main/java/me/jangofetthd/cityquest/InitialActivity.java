package me.jangofetthd.cityquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);



        if(isOk()){
            Intent goMain = new Intent(this, MainActivity.class);
            startActivity(goMain);
        }
    }

    private boolean isOk(){
        return true;
    }
}

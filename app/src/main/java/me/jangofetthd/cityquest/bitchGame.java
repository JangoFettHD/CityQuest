package me.jangofetthd.cityquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class bitchGame extends AppCompatActivity {

    TextView clicks;
    ImageView bitch;
    int cl =10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitch_game);
        clicks = (TextView)findViewById(R.id.clicks);
        bitch = (ImageView) findViewById(R.id.bitch);

        cl= 20 + (int)(Math.random() * 100);
        clicks.setText(Integer.toString(cl));

        final Intent intent = new Intent(this, MainActivity.class);

        bitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cl==1){
                    Toast.makeText(bitchGame.this, "ЕЕЕЕЕ РОК!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                cl=cl-1;
                clicks.setText(Integer.toString(cl));
                }
            }
        });

    }
}

package me.jangofetthd.cityquest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class bitchGame extends AppCompatActivity {

    TextView clicks;
    ImageView bitch;
    ProgressBar progressBar;
    int cl =10;
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitch_game);
        clicks = (TextView)findViewById(R.id.clicks);
        bitch = (ImageView) findViewById(R.id.bitch);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        cl= 20 + (int)(Math.random() * 120);
        max= cl;
        clicks.setText("100");

        final Intent intent = new Intent(this, MainActivity.class);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        final Animation animation_mini = AnimationUtils.loadAnimation(this, R.anim.scale_mini);

        bitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bitch.startAnimation(animation);
                clicks.startAnimation(animation_mini);
                Log.i("cl", Integer.toString(cl));
                progressBar.setProgress(cl*100/max);
                clicks.setText(Integer.toString(cl*100/max));
                if (cl==1){
                    Toast.makeText(bitchGame.this, "ЕЕЕЕЕ РОК!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    cl+=-1;
                    Log.i("cl", Integer.toString(cl)+"    "+(max*75/100));
                    if (cl==max*75/100)bitch.setImageDrawable(getResources().getDrawable(R.drawable.bitch75));
                    if (cl==max*50/100)bitch.setImageDrawable(getResources().getDrawable(R.drawable.bitch50));
                    if (cl==max*25/100)bitch.setImageDrawable(getResources().getDrawable(R.drawable.bitch25));
                }
            }
        });

    }
}

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
import android.widget.Toast;

public class subwayGame extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;
    int clicks;
    int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_game);

        imageView = (ImageView)findViewById(R.id.subway);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        clicks= 70 + (int)(Math.random() * 150);
        Log.i("cl", Integer.toString(clicks));
        max=clicks;

        final Intent intent = new Intent(this, MainActivity.class);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageView.startAnimation(animation);
                Log.i("cl", Integer.toString(clicks));
                progressBar.setProgress(clicks*100/max);
                if (clicks==1){
                    Toast.makeText(subwayGame.this, "ЕЕЕЕЕ РОК!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    clicks+=-1;
                    Log.i("cl", Integer.toString(clicks)+"    "+(max*75/100));
                    if (clicks==max*75/100)imageView.setImageDrawable(getResources().getDrawable(R.drawable.subway75));
                    if (clicks==max*50/100)imageView.setImageDrawable(getResources().getDrawable(R.drawable.subway50));
                    if (clicks==max*25/100)imageView.setImageDrawable(getResources().getDrawable(R.drawable.subway25));
                }
            }
        });
    }
}

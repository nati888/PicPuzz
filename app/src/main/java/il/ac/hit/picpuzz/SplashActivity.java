package il.ac.hit.picpuzz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends Activity {
    private int SPLASH_SCREEN = 2300;
    private Animation botAnim;
    private LottieAnimationView puzzAnim;
    private TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        puzzAnim = findViewById(R.id.puzzle_anim);
        puzzAnim.setSpeed(2);

        logo = findViewById(R.id.name_logo);
        logo.setAnimation(botAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
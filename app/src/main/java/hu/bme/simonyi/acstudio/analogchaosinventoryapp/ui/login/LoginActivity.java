package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.HomeActivityIntentFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * Activity for login
 *
 * @author Lajos Nyeki
 */
public class LoginActivity extends RoboActivity {

    private static final int ANIMATION_FRAME_DELAY = 40;
    private static final int ANIMATION_TIME_MS = 1000;

    @InjectView(R.id.ac_logo_login)
    private ImageView acLogoImage;

    @InjectView(R.id.login_fields_layout)
    private LinearLayout fieldsLayout;

    @InjectView(R.id.edit_email_login)
    private EditText emailEditText;

    @InjectView(R.id.edit_password_login)
    private EditText passwordEdittext;

    @InjectView(R.id.btn_login)
    private Button loginButton;

    private AnimationRunnable animationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        animationRunnable = new AnimationRunnable();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivityIntentFactory.createHomeActivityIntent(getApplicationContext()));
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        animationRunnable.startAnimation();
    }

    private class AnimationRunnable implements Runnable {

        private static final float OVERSHOOT_RATIO = 1.6f;
        private static final float STEPS = ANIMATION_TIME_MS / ANIMATION_FRAME_DELAY;

        private int frameCount = 0;

        private boolean enabled = true;

        private float deltaX;
        private float initX;
        private OvershootInterpolator interpolator = new OvershootInterpolator(OVERSHOOT_RATIO);

        private Handler handler;

        public void startAnimation() {
            init();
            if (enabled) {
                this.handler = new Handler();
                acLogoImage.setX(initX);
                acLogoImage.invalidate();
                handler.postDelayed(this, ANIMATION_FRAME_DELAY);
            }
        }

        private void init() {
            deltaX = (fieldsLayout.getWidth() + acLogoImage.getWidth()) / 2.0f;
            initX = acLogoImage.getWidth() * -1;
        }

        @Override
        public void run() {
            acLogoImage.setX(initX + deltaX * interpolator.getInterpolation(frameCount / STEPS));
            acLogoImage.invalidate();
            if (frameCount < STEPS) {
                frameCount++;
                handler.postDelayed(this, ANIMATION_FRAME_DELAY);
            } else {
                fieldsLayout.setVisibility(View.VISIBLE);
                enabled = false;
            }
        }
    }
}

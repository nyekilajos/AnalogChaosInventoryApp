package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.Logger;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.LoggerFactory;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.GenericServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.LoginServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.HomeActivityIntentFactory;
import roboguice.RoboGuice;
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

    private static final Logger LOGGER = LoggerFactory.createLogger(LoginActivity.class);

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

    @Inject
    private LocalSettingsService localSettingsService;

    private AnimationRunnable animationRunnable;

    private View.OnClickListener onLoginClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            localSettingsService.setEmailAddress(emailEditText.getText().toString());

            emailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    emailEditText.setError(null);
                }
            });

            passwordEdittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    passwordEdittext.setError(null);
                }
            });

            boolean loginValid = true;
            if (!isEmailValid()) {
                emailEditText.setError(getString(R.string.email_validation_error));
                loginValid = false;
            }
            if (!isPasswordNotEmpty()) {
                passwordEdittext.setError(getString(R.string.password_validation_error));
                loginValid = false;
            }
            if (loginValid) {
                startLoginCommunication();
            }
        }
    };

    private GenericServerCommunicationTask.CommunicationStatusHandler<LoginResponse> statusHandler = new GenericServerCommunicationTask.CommunicationStatusHandler<LoginResponse>() {
        @Override
        public void onPreExecute() throws Exception {

        }

        @Override
        public void onSuccess(LoginResponse loginResponse) throws Exception {
            LOGGER.debug("Login successful");
            startActivity(HomeActivityIntentFactory.createHomeActivityIntent(getApplicationContext()));
        }

        @Override
        public void onThrowable(Throwable t) throws RuntimeException {
            LOGGER.error(t.toString());
        }

        @Override
        public void onFinally() throws RuntimeException {

        }
    };

    private void startLoginCommunication() {
        LoginServerCommunicationTask loginTask = RoboGuice.getInjector(this).getInstance(LoginServerCommunicationTask.class);
        loginTask.setStatusHandler(statusHandler);
        loginTask.login(emailEditText.getText().toString(), passwordEdittext.getText().toString());
    }

    private boolean isPasswordNotEmpty() {
        return !passwordEdittext.getText().toString().isEmpty();
    }

    private boolean isEmailValid() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        animationRunnable = new AnimationRunnable();
        loginButton.setOnClickListener(onLoginClickListener);
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

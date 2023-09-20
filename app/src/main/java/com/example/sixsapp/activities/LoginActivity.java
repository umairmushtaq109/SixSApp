package com.example.sixsapp.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.appupdate.manager.DownloadManager;
import com.example.sixsapp.BuildConfig;
import com.example.sixsapp.R;
import com.example.sixsapp.adapters.ImageSliderAdapter;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Product;
import com.example.sixsapp.pojo.User;
import com.example.sixsapp.utilis.General;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.SweetAlert;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.OnDialogButtonClickListener;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ImageSliderAdapter adapter;
    private List<Integer> images = new ArrayList<>();
    private int currentPage = 0;
    private Timer timer;
    private boolean isPaused = false;
    private Button mLoginButton;
    private TextInputEditText mCardNumber, mPin;
    ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


        // Camera Permission
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(LoginActivity.this)
                        .withTitle("Camera permission")
                        .withMessage("Camera permission is needed to take pictures of 6S findings")
                        .withButtonText(android.R.string.ok, new OnDialogButtonClickListener() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getApplication().getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        Dexter.withContext(LoginActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(dialogPermissionListener)
                .check();

        // EasyCamera require permission for external storage in Android < 10
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Dexter.withContext(LoginActivity.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            finish();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    })
                    .check();
        }

        //Check for Updates
//        AppUpdater appUpdater = new AppUpdater(this)
//                .setDisplay(Display.DIALOG)
//                .setUpdateFrom(UpdateFrom.JSON)
//                .setTitleOnUpdateAvailable("Update available")
//                .setContentOnUpdateAvailable("Install the latest version available of 6S App")
//                .setButtonUpdate("Update now?")
//	              .setIcon(R.mipmap.ic_launcher) // Notification icon
//                .setCancelable(false);
//        appUpdater.start();

        if(Global.isUpdateAvailable(LoginActivity.this)){

        } else {
            Toast.makeText(this, "No Update Available", Toast.LENGTH_SHORT).show();
        }



        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!General.isInternetAvailable(LoginActivity.this)){
                    new SweetAlert(LoginActivity.this)
                            .showErrorAlert("Connection Failed", "Failed to connect to internet");
                    return;
                }
                if(TextUtils.isEmpty(mCardNumber.getText())){
                    mCardNumber.setError("Card Number is required");
                    return;
                }
                if(TextUtils.isEmpty(mPin.getText())){
                    mPin.setError("Pin Code is required");
                    return;
                }

                Login(mCardNumber.getText().toString(), mPin.getText().toString());
            }
        });
    }

    private void init() {
        mApiService = ApiClient.getClient().create(ApiService.class);

        mLoginButton = findViewById(R.id.buttonLogin);
        mCardNumber = findViewById(R.id.cardNumber_Input);
        mPin = findViewById(R.id.pinCode_Input);

        viewPager = findViewById(R.id.viewPager);

        images.add(R.drawable.login_image);
        images.add(R.drawable.six_1);
        images.add(R.drawable.six_2);
        images.add(R.drawable.six_3);
        images.add(R.drawable.six_4);
        images.add(R.drawable.six_5);
        images.add(R.drawable.six_6);

        adapter = new ImageSliderAdapter(this, images);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    pauseAutoSlide();
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    resumeAutoSlide();
                }
            }
        });

        autoSlideImages();
    }

    private void Login(String card, String pin) {
        SweetAlert mLoading = new SweetAlert(this);
        mLoading.showProgressAlert("Please wait...");
        Call<User> call = mApiService.Login(card, pin);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    mLoading.dismissAlert();
                    Global.User = response.body();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (response.code() == 404) {
                    mLoading.dismissAlert();
                    new SweetAlert(LoginActivity.this)
                            .showErrorAlert("Invalid Credentials", "Please enter valid Card No and Pin");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mLoading.dismissAlert();
                new SweetAlert(LoginActivity.this)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }

    private void autoSlideImages() {
        final int numPages = images.size();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isPaused) {
                            currentPage = (currentPage + 1) % numPages;
                            viewPager.setCurrentItem(currentPage);
                        }
                    }
                });
            }
        }, 5000, 5000); // Auto-slide every 3 seconds
    }

    private void pauseAutoSlide() {
        isPaused = true;
    }

    private void resumeAutoSlide() {
        isPaused = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.action_about){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
            View dialogView = inflater.inflate(R.layout.dialog_app_info, null);

            // Set the custom layout to the dialog builder
            builder.setView(dialogView);

            // Retrieve views from the custom layout
            ImageView logoImageView = dialogView.findViewById(R.id.imageViewLogo);
            TextView versionTextView = dialogView.findViewById(R.id.textViewVersion);

            // Set app-specific information
            // Replace with your app's logo and version
            logoImageView.setImageResource(R.mipmap.ic_launcher_round);
            versionTextView.setText("Version " + BuildConfig.VERSION_NAME);

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (id == R.id.action_fwoh){
            Global.Open5W1H(LoginActivity.this);
        }

        return true;
    }
}
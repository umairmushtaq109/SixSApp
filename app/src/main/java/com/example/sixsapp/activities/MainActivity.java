package com.example.sixsapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixsapp.R;
import com.example.sixsapp.adapters.GridAdapter;
import com.example.sixsapp.adapters.ImageSliderAdapter;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.dialogs.InitiateDialog;
import com.example.sixsapp.dialogs.RemarksDialog;
import com.example.sixsapp.pojo.Category;
import com.example.sixsapp.pojo.Department;
import com.example.sixsapp.pojo.InitiateResponse;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.SweetAlert;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements InitiateDialog.DialogListener {
    GridAdapter mAdapter;
    private List<String> titles;
    private List<Category> categories;
    private static final int REQUEST_CODE_REFRESH_ACTIVITY = 1;
    private static final long BACK_PRESS_DELAY = 2000; // Time in milliseconds
    private long lastBackPressTime = 0;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubmit = findViewById(R.id.buttonSubmit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemarksDialog customDialog = new RemarksDialog(MainActivity.this);
                customDialog.show();
            }
        });

        ShowDialog();
    }



    private void ShowDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        InitiateDialog dialogFragment = InitiateDialog.newInstance();
        dialogFragment.setCancelable(false);
        dialogFragment.show(fragmentManager, "InitiateAuditDialog");
    }

    private void GetCategories(InitiateResponse initiateResponse) {
        SweetAlert mLoading = new SweetAlert(MainActivity.this);
        mLoading.showProgressAlert("Loading...");
        RecyclerView mRecyclerView = findViewById(R.id.grid_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Category>> call = mApiService.GetCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                int DisabledCount = 0;
                if(initiateResponse.getSort() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getSort())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }
                if(initiateResponse.getSetInOrder() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getSetInOrder())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }
                if(initiateResponse.getSafety() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getSafety())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }
                if(initiateResponse.getShine() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getShine())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }
                if(initiateResponse.getStandard() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getStandard())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }
                if(initiateResponse.getSustain() != 0){
                    categories.stream()
                            .filter(category -> category.getCategoryID() == initiateResponse.getSustain())
                            .forEach(category -> category.setDisable(true));
                    DisabledCount++;
                }

                if(DisabledCount == categories.size()){
                    mSubmit.setVisibility(View.VISIBLE);
                }

                mAdapter = new GridAdapter(getApplicationContext(), categories, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mLoading.dismissAlert();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                mLoading.dismissAlert();
                new SweetAlert(MainActivity.this)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GetCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REFRESH_ACTIVITY) {
            if (resultCode == 0) {
                RefreshCategories();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.action_settings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        if (id == R.id.action_dialog){
            ShowDialog();
        }
        if (id == R.id.action_reports){
            startActivity(new Intent(MainActivity.this, ReportsActivity.class));
        }
        return true;
    }

    @Override
    public void onDialogResponse(boolean isValid, InitiateResponse initiateResponse) {
        if(isValid && initiateResponse != null){
            GetCategories(initiateResponse);
            Global.ResultID = initiateResponse.getResultID();

            LinearLayout mLinear = findViewById(R.id.linear_display);
            mLinear.setVisibility(View.VISIBLE);

            TextView Date = findViewById(R.id.Day_textView);
            TextView Department = findViewById(R.id.Department_textView);
            TextView Section = findViewById(R.id.Section_textView);

            String formattedAuditDate = Global.FormatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", Global.AuditDate);
            Date.setText(formattedAuditDate);

            Department.setText(Global.SeletedDepartment.getDepartmentName());
            Section.setText(Global.SeletedSection.getSectionName());
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastBackPressTime > BACK_PRESS_DELAY) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            lastBackPressTime = currentTime;
        } else {
            super.onBackPressed(); // Close the activity
        }
    }

    public void RefreshCategories(){
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);

        Call<InitiateResponse>  call = mApiService.SaveMainResult(Global.AuditDate,
                Global.SeletedDepartment.getDepartmentID(),
                Global.SeletedSection.getSectionID(),
                Global.User.getCardNo());
        call.enqueue(new Callback<InitiateResponse>() {
            @Override
            public void onResponse(Call<InitiateResponse> call, Response<InitiateResponse> response) {
                InitiateResponse initiateResponse = response.body();
                GetCategories(initiateResponse);
            }

            @Override
            public void onFailure(Call<InitiateResponse> call, Throwable t) {

            }
        });
    }
}
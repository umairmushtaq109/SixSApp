package com.example.sixsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.example.sixsapp.R;
import com.example.sixsapp.adapters.ReportAdapter;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Department;
import com.example.sixsapp.pojo.ReportResponse;
import com.example.sixsapp.pojo.Section;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.SweetAlert;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends AppCompatActivity {
    AutoCompleteTextView departmentAutoCompleteTextView, sectionAutoCompleteTextView;
    TextInputLayout departmentDropdownContainer, sectionDropdownContainer;
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DividerItemDecoration dividerItemDecoration;
    View emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the back button
        }
        emptyView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        departmentAutoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        sectionAutoCompleteTextView = findViewById(R.id.autoCompleteTextView_section);
        departmentDropdownContainer = findViewById(R.id.dropdown_container);
        sectionDropdownContainer = findViewById(R.id.dropdown_section);

        departmentDropdownContainer.setEndIconOnClickListener(v -> departmentAutoCompleteTextView.showDropDown());
        sectionDropdownContainer.setEndIconOnClickListener(v -> sectionAutoCompleteTextView.showDropDown());

        departmentAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Department selectedDepartment = (Department) parent.getItemAtPosition(position);
                sectionAutoCompleteTextView.setText("");
                Global.PopulateSectionSpinner(selectedDepartment.getDepartmentID(), getApplicationContext(), sectionAutoCompleteTextView);
                FilterAuditResults(selectedDepartment.getDepartmentID(), 2);
            }
        });

        sectionAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Section selectedSection = (Section) adapterView.getItemAtPosition(i);
                FilterAuditResults(selectedSection.getSectionID(), 3);
            }
        });
    }

    private void FilterAuditResults(int id, int option) {
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ReportResponse>> call = mApiService.FilterReport(id, option, Global.User.getCardNo());
        call.enqueue(new Callback<List<ReportResponse>>() {
            @Override
            public void onResponse(Call<List<ReportResponse>> call, Response<List<ReportResponse>> response) {
                List<ReportResponse> responses = response.body();
                if (responses.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                ReportAdapter adapter = new ReportAdapter(getApplicationContext(), responses);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ReportResponse>> call, Throwable t) {
                new SweetAlert(ReportsActivity.this)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Global.PopulateDepartmentSpinner(this, departmentAutoCompleteTextView);
        FilterAuditResults(0, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.reports_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.action_clear){
            FilterAuditResults(0, 1);
            departmentAutoCompleteTextView.setText("");
            sectionAutoCompleteTextView.setText("");
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Call the default back button behavior
        }
        return true;
    }
}
package com.example.sixsapp.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sixsapp.R;
import com.example.sixsapp.activities.LoginActivity;
import com.example.sixsapp.activities.MainActivity;
import com.example.sixsapp.activities.ReportsActivity;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Department;
import com.example.sixsapp.pojo.InitiateResponse;
import com.example.sixsapp.pojo.Section;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.SweetAlert;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitiateDialog extends DialogFragment {
    private Context context;
    AutoCompleteTextView departmentAutoCompleteTextView, sectionAutoCompleteTextView;
    TextInputLayout departmentDropdownContainer, sectionDropdownContainer;
    Department selectedDepartment;
    Section selectedSection;

    CalendarView mCalenderView;

    Button mInitiateButton, mClose, mViewReports;

    private boolean isValid = false;

    Date selectedDate;

    public static InitiateDialog newInstance() {
        return new InitiateDialog();
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        context = requireContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_initiate_audit, null);

        departmentAutoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView);
        sectionAutoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView_section);
        departmentDropdownContainer = dialogView.findViewById(R.id.dropdown_container);
        sectionDropdownContainer = dialogView.findViewById(R.id.dropdown_section);
        mCalenderView = dialogView.findViewById(R.id.calendarView);
        mInitiateButton = dialogView.findViewById(R.id.buttonInitiate);
        mClose = dialogView.findViewById(R.id.buttonClose);
        mViewReports = dialogView.findViewById(R.id.buttonViewReport);

        departmentDropdownContainer.setEndIconOnClickListener(v -> departmentAutoCompleteTextView.showDropDown());

        departmentAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = (Department) parent.getItemAtPosition(position);
                sectionAutoCompleteTextView.setText("");
                Global.PopulateSectionSpinner(selectedDepartment.getDepartmentID(), context, sectionAutoCompleteTextView);
            }
        });

        sectionAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSection = (Section) adapterView.getItemAtPosition(i);
            }
        });

        // Disable previous dates
        mCalenderView.setMinDate(System.currentTimeMillis() - 1000);

        // Disable future dates
        mCalenderView.setMaxDate(System.currentTimeMillis());

        mCalenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date change here
                // String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                selectedDate = selectedCalendar.getTime();
            }
        });

        mInitiateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedDepartment != null && selectedSection != null){
                    InitiateAudit(selectedDate,
                            Global.User.getCardNo(),
                            selectedDepartment.getDepartmentID(),
                            selectedSection.getSectionID());
                } else {
                    Toast.makeText(context, "Select a Department and Section", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ReportsActivity.class));
            }
        });

        return new MaterialAlertDialogBuilder(context)
                .setTitle("Select Department and Audit Date")
                .setView(dialogView)
                .create();
    }

    public void InitiateAudit(Date AuditDate, int AuditorID, int DepartmentID, int SectionID){
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);

        if(AuditDate == null){
            AuditDate = new Date();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String formattedDate = dateFormat.format(AuditDate);

        Call<InitiateResponse>  call = mApiService.SaveMainResult(formattedDate, DepartmentID, SectionID, AuditorID);
        call.enqueue(new Callback<InitiateResponse>() {
            @Override
            public void onResponse(Call<InitiateResponse> call, Response<InitiateResponse> response) {
                InitiateResponse initiateResponse = response.body();

                Global.SeletedDepartment = selectedDepartment;
                Global.SeletedSection = selectedSection;
                Global.AuditDate = formattedDate;

                if(initiateResponse.getIsSubmitted() == 1){
                    Toast.makeText(context, "Audit has already been completed!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = true;
                    DialogListener listener = (DialogListener) getActivity();
                    listener.onDialogResponse(isValid, initiateResponse);
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<InitiateResponse> call, Throwable t) {
                new SweetAlert((Activity) context)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }

    public interface DialogListener {
        void onDialogResponse(boolean isValid, InitiateResponse initiateResponse);
    }

    @Override
    public void onStart() {
        super.onStart();
        Global.PopulateDepartmentSpinner(context, departmentAutoCompleteTextView);
    }
}

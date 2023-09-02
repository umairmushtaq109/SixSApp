package com.example.sixsapp.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.sixsapp.R;
import com.example.sixsapp.activities.MainActivity;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.SweetAlert;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemarksDialog {
    private Context context;

    public RemarksDialog(Context context) {
        this.context = context;
    }

    public void show() {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_remarks, null);

        TextInputEditText multilineEditText = dialogView.findViewById(R.id.multilineEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("6S Audit Submission");
        builder.setView(dialogView);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String remarks = multilineEditText.getText().toString();
                SubmitAudit(remarks);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void SubmitAudit(String remarks){
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = mApiService.SubmitAudit(Global.ResultID, remarks);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText((Activity) context, "Audit has been submitted", Toast.LENGTH_SHORT).show();
                    ((Activity) context).finish();
                    ((Activity) context).startActivity(new Intent((Activity) context, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new SweetAlert((Activity) context)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }
}

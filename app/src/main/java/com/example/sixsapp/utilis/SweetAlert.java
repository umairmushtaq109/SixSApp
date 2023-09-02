package com.example.sixsapp.utilis;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetAlert {
    private Context context;
    private SweetAlertDialog pDialog;

    public SweetAlert(Context context){
        this.context = context;
    }

    public void showProgressAlert(String title){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(title);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void showGeneralAlert(String title, String subTitle){
        pDialog = new SweetAlertDialog(context);
        pDialog.setTitleText(title);
        pDialog.setContentText(subTitle);
        pDialog.show();
    }

    public void showErrorAlert(String title, String subTitle){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(subTitle);
        pDialog.show();
    }

    public void showSuccessAlert(String title, String subTitle){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(subTitle);
        pDialog.show();
    }

    public void showSuccessAlertWithClick(String title, String subTitle, SweetAlertDialog.OnSweetClickListener clickListener){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(title);
        pDialog.setContentText(subTitle);
        pDialog.setConfirmClickListener(clickListener);
        pDialog.show();
    }

    public void dismissAlert(){
        pDialog.dismissWithAnimation();
    }
}

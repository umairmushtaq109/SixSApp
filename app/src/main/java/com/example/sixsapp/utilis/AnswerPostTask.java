package com.example.sixsapp.utilis;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sixsapp.R;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Answer;
import com.example.sixsapp.pojo.Pair;
import com.example.sixsapp.pojo.Question;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerPostTask extends AsyncTask<Void, Void, List<Answer>> {
    private Context context;
    private List<Pair> pairList;
    SweetAlert sweetAlert;

    public AnswerPostTask(Context context, List<Pair> pairList) {
        this.context = context;
        this.pairList = pairList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sweetAlert = new SweetAlert(context);
        sweetAlert.showProgressAlert("Upload data...");
    }

    @Override
    protected List<Answer> doInBackground(Void... voids) {
        List<Answer> answersToSubmit = new ArrayList<>();
        for (int i = 0; i < pairList.size(); i++) {
            Question question = pairList.get(i).getQuestion();
            Answer answer = pairList.get(i).getAnswer();
            answer.setResultID(Global.ResultID);
            answer.setCategoryID(Global.SelectedCategory.CategoryID);
            answer.setQuestionID(question.QuestionID);
            List<String> imageBase64s = new ArrayList<>();
            //List<byte[]> imageBytes = new ArrayList<>();
            for (Uri uri : answer.getImageUris()) {
                if (uri != null) {
                    imageBase64s.add(Global.encodeImageToBase64(context, uri).trim());
//                    byte[] imageData = Global.UriToByte(context, uri);
//                    imageBytes.add(imageData);
                } else {
                    imageBase64s.add(Global.DrawableToBase64(context, R.drawable.add_image_placeholder));
                    //imageBase64s.add(null);
                    //imageBytes.add(null);
                }
            }
            answer.setImageBase64(imageBase64s);
            //answer.setImagesBytes(imageBytes);
            answersToSubmit.add(answer);
        }
        return answersToSubmit;
    }

    @Override
    protected void onPostExecute(List<Answer> answersToSubmit) {
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = mApiService.SaveQuestionnaire(answersToSubmit);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    sweetAlert.dismissAlert();
                    new SweetAlert(context).showSuccessAlertWithClick("Successful", "Data uploaded successfully", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        }
                    });
                } else {
                    try {
                        String errorResponseJson = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorResponseJson);
                        String errorMessage = jsonObject.getString("Message").replaceAll("\"", "");
                        sweetAlert.dismissAlert();
                        new SweetAlert(context).showErrorAlert("Error", errorMessage);
                        // Display the error message to the user
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "Retrofit POST: " + t.getMessage());
                sweetAlert.dismissAlert();
                new SweetAlert(context).showErrorAlert("Error", "Server Error Occurred");
            }
        });
    }
}

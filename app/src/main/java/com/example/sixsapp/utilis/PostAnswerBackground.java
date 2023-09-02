package com.example.sixsapp.utilis;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Answer;
import com.example.sixsapp.pojo.Pair;
import com.example.sixsapp.pojo.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAnswerBackground {
    public void postInBackground(Context context, List<Pair> pairList) {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                post(context, pairList);
            }
        });
        backgroundThread.start();
    }

    public void post(Context context, List<Pair> pairList) {
//        SweetAlert sweetAlert = new SweetAlert(context);
//        sweetAlert.showProgressAlert("Upload data...");
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        List<Answer> answersToSubmit = new ArrayList<>();
        for (int i = 0; i < pairList.size(); i++) {
            Question question = pairList.get(i).getQuestion();
            Answer answer = pairList.get(i).getAnswer();
//            answer.setCategoryID(Global.CategoryID);
            answer.setQuestionID(question.QuestionID);
            List<String> imageBase64s = new ArrayList<>();
            for (Uri uri:answer.getImageUris()) {
                if(uri != null){
                    imageBase64s.add(Global.encodeImageToBase64(context, uri));
                } else {
                    imageBase64s.add(null);
                }
            }
            answer.setImageBase64(imageBase64s);
            answersToSubmit.add(answer);
        }

        try {
            Call<Void> call = mApiService.uploadImageModel(answersToSubmit);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "Retrofit POST: " + t.getMessage());
                }
            });
        } catch (Exception ex){

        }
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()){
//                    sweetAlert.dismissAlert();
//                    new SweetAlert(context).showSuccessAlert("Successfull", "Data uploaded successfully");
//                } else {
//                    sweetAlert.dismissAlert();
//                    new SweetAlert(context).showErrorAlert("Successfull", response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                sweetAlert.dismissAlert();
//                new SweetAlert(context).showErrorAlert("Successfull", "Server error occurred");
//            }
//        });

    }
}

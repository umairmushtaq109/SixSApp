package com.example.sixsapp.utilis;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AnswerPostRxJava {
    private CompositeDisposable disposables = new CompositeDisposable();
    private SweetAlert sweetAlert;


    public void post(Context context, List<Pair> pairList, OnTaskCompleted listener) {
        sweetAlert = new SweetAlert(context);
        sweetAlert.showProgressAlert("Upload data...");

        List<Answer> answersToSubmit = new ArrayList<>();

        for (int i = 0; i < pairList.size(); i++) {
            Question question = pairList.get(i).getQuestion();
            Answer answer = pairList.get(i).getAnswer();
//            answer.setCategoryID(Global.CategoryID);
            answer.setQuestionID(question.QuestionID);
            List<String> imageBase64s = new ArrayList<>();
            for (Uri uri : answer.getImageUris()) {
                if (uri != null) {
                    imageBase64s.add(Global.encodeImageToBase64(context, uri));
                } else {
                    imageBase64s.add(null);
                }
            }
            answer.setImageBase64(imageBase64s);
            answersToSubmit.add(answer);
        }

        ApiService mApiService = ApiClient.getClient().create(ApiService.class);

        mApiService.uploadImageModelObservable(answersToSubmit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                (response) -> {
                    // On success
                    sweetAlert.dismissAlert();
                    listener.onTaskCompleted(true, "Data uploaded successfully");
                },
                throwable -> {
                    // On error
                    Log.d(TAG, "Retrofit POST: " + throwable.getMessage());
                    sweetAlert.dismissAlert();
                    listener.onTaskCompleted(false, "Error: " + throwable.getMessage());
                }
            );
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(boolean isSuccess, String message);
    }

    public void dispose() {
        disposables.dispose();
    }
}

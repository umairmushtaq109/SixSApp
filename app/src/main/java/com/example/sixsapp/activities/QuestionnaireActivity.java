package com.example.sixsapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixsapp.R;
import com.example.sixsapp.adapters.QuestionnaireAdapter;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Answer;
import com.example.sixsapp.pojo.Category;
import com.example.sixsapp.pojo.Pair;
import com.example.sixsapp.pojo.Question;
import com.example.sixsapp.utilis.AnswerPostTask;
import com.example.sixsapp.utilis.Global;
import com.example.sixsapp.utilis.PermissionHelper;
import com.example.sixsapp.utilis.SweetAlert;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionnaireActivity extends AppCompatActivity {

    private PermissionHelper permissionHelper;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private QuestionnaireAdapter adapter;
    List<Pair> pairs;
    EasyImage easyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        init();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the back button
        }
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        easyImage = new EasyImage.Builder(getApplicationContext())
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false) // THIS requires granting WRITE_EXTERNAL_STORAGE permission for devices running Android 9 or lower
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("SixS App")
                .allowMultiple(false)
                .build();

        GetQuestionnaire();
    }

    private void GetQuestionnaire(){
        SweetAlert mLoading = new SweetAlert(QuestionnaireActivity.this);
        mLoading.showProgressAlert("Loading...");

        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Question>> call = mApiService.GetQuestionnaire(Global.SelectedCategory.getCategoryID());

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                pairs = new ArrayList<>();
                for (Question question: response.body()) {
                    pairs.add(new Pair(question, new Answer()));
                }
                adapter = new QuestionnaireAdapter(QuestionnaireActivity.this, QuestionnaireActivity.this, pairs);
                recyclerView.setAdapter(adapter);
                mLoading.dismissAlert();
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                mLoading.dismissAlert();
                new SweetAlert(QuestionnaireActivity.this)
                        .showErrorAlert("Error", "Some error occurred, Try Again!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode >= 0 && requestCode < pairs.size()) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Uri imageUri = data.getData();
//            int height = imageBitmap.getHeight();
//            //Uri selectedImageUri = data.getData();
//            adapter.setSelectedImageUri(requestCode, imageBitmap, Global.PICTURE_NO);
//            //adapter.notifyItemChanged(requestCode); // Notify the adapter of the change
//        }

        adapter.easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
//                for (MediaFile imageFile : imageFiles) {
//                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
//                }
                URI imageURI = imageFiles[0].getFile().toURI();
                Uri androidImageUri = Uri.parse(imageURI.toString());
                adapter.setSelectedImageUri(Global.ITEM_POSITION, androidImageUri, Global.PICTURE_NO);
                //onPhotosReturned(imageFiles);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_submit) {
            //new AnswerPostTask(getApplicationContext(), adapter.pairList).execute();
            //new PostAnswerBackground().postInBackground(getApplicationContext(), adapter.pairList);

            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Make sure the questionnaire is filled properly")
                    .setCancelText("Cancel")
                    .setCancelButtonBackgroundColor(Color.parseColor("#F27474"))
                    .setConfirmButton("Proceed", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            new AnswerPostTask(QuestionnaireActivity.this, adapter.pairList).execute();
                            sweetAlertDialog.cancel();
                        }
                    })
                    .setConfirmButtonBackgroundColor(Color.parseColor("#A5DC86"))
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    });
            dialog.show();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Call the default back button behavior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAutoCacheClearEnabled = sharedPreferences.getBoolean("auto_cache_clear", true);

        if (isAutoCacheClearEnabled) {
            clearCache("/EasyImage");
            clearCache("/images");
        }
    }

    private void clearCache(String FolderName) {
        String rootDataDir = getApplicationContext().getCacheDir().getAbsolutePath() + FolderName;
        File cacheFolder = new File(rootDataDir);
        if(cacheFolder.exists()){
            File[] cachedImages = cacheFolder.listFiles();
            if(cachedImages != null && cachedImages.length > 0){
                for (File file: cachedImages) {
                    file.delete();
                }
                Toast.makeText(this, "Cache Cleared!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
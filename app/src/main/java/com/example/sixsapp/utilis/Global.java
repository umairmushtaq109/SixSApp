package com.example.sixsapp.utilis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;

import com.example.sixsapp.R;
import com.example.sixsapp.api.ApiClient;
import com.example.sixsapp.api.ApiService;
import com.example.sixsapp.pojo.Category;
import com.example.sixsapp.pojo.Department;
import com.example.sixsapp.pojo.Section;
import com.example.sixsapp.pojo.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Global {
    public static User User;
    public static Category SelectedCategory;
    public static Department SeletedDepartment;
    public static Section SeletedSection;
    public static int ResultID;
    public static String AuditDate;


    public static int PICTURE_NO, ITEM_POSITION;

    public static byte[] UriToByte(Context context, Uri imageUri){
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex){
            return null;
        }
    }

    public static String DrawableToBase64(Context context, int imageDrawable){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageDrawable);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        String baseString = Base64.encodeToString(byteArray,Base64.DEFAULT);
        return baseString;
    }

    public static byte[] BitmapToByte(Bitmap bitmap){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex){
            return null;
        }
    }

    public static String encodeImageToBase64(Context context, Uri imageUri) {
        try {
            // Create a File instance from the image URI
            File originalImageFile = new File(imageUri.getPath());

            // Use Compressor to compress the image
            File compressedImageFile = new Compressor(context)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(originalImageFile);

            byte[] imageBytes = new byte[(int) compressedImageFile.length()];
            FileInputStream fileInputStream = new FileInputStream(compressedImageFile);
            fileInputStream.read(imageBytes);
            fileInputStream.close();

            // Encode the compressed image bytes to Base64
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String BitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        bitmap.recycle();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void Open5W1H(Context context){
        // Define the URL you want to open
        String urlToOpen = "http://192.168.50.5:5000/FiveWOneH/WorkOrder";
        // Create an Intent with the ACTION_VIEW action and the URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen));
        context.startActivity(intent);
    }
    public static File createImageFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg",        /* suffix */
                    storageDir     /* directory */
            );
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void PopulateDepartmentSpinner(Context context, AutoCompleteTextView textView){
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Department>> call = mApiService.GetDepartments();
        call.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if(response.isSuccessful()){
                    List<Department> departmentList = response.body();
                    ArrayAdapter<Department> adapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_dropdown_item_1line, departmentList
                    );
                    textView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {

            }
        });
    }

    public static void PopulateSectionSpinner(int departmentID, Context context, AutoCompleteTextView textView){
        ApiService mApiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Section>>  call = mApiService.GetSections(departmentID);
        call.enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {
                if(response.isSuccessful()){
                    List<Section> sectionList = response.body();
                    ArrayAdapter<Section> adapter = new ArrayAdapter<>(
                            context, android.R.layout.simple_dropdown_item_1line, sectionList
                    );
                    textView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {

            }
        });
    }

    public static String FormatDate(String inPattern, String outPattern, String Date){
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern, Locale.US);

        try {
            Date date = inputFormat.parse(Date);
            String formattedDate = outputFormat.format(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Invalid Date";
    }
}

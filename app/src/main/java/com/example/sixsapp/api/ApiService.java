package com.example.sixsapp.api;

import com.example.sixsapp.pojo.Answer;
import com.example.sixsapp.pojo.Category;
import com.example.sixsapp.pojo.Department;
import com.example.sixsapp.pojo.InitiateResponse;
import com.example.sixsapp.pojo.Product;
import com.example.sixsapp.pojo.Question;
import com.example.sixsapp.pojo.ReportResponse;
import com.example.sixsapp.pojo.Section;
import com.example.sixsapp.pojo.UpdateInfo;
import com.example.sixsapp.pojo.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import kotlin.jvm.JvmSuppressWildcards;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
    @GET("api/SixSLean/Login")
    Call<User> Login(@Query("CardNo") String card, @Query("Pin") String pin);

    @GET("api/SixSLean/GetCategoryList")
    Call<List<Category>> GetCategories();

    @GET("api/SixSLean/RetrieveDepartments")
    Call<List<Department>> GetDepartments();

    @GET("api/SixSLean/RetrieveSections")
    Call<List<Section>> GetSections(@Query("DepartmentID") int DepartmentID);

    @POST("api/SixSLean/SaveMainResult")
    Call<InitiateResponse> SaveMainResult(@Query("AuditDate") String AuditDate, //Passing Date as String to DateTime Type in .NET
                                          @Query("DepartmentID") int DepartmentID,
                                          @Query("SectionID") int SectionID,
                                          @Query("AuditorID") int AuditorID);

    @POST("api/SixSLean/SaveFindingsAttachments")
    Call<Void> SaveQuestionnaire(@Body List<Answer> answerModel);

    @GET("api/SixSLean/GetQuestionnaire")
    Call<List<Question>> GetQuestionnaire(@Query("Category") int CategoryID);

    @POST("api/SixSLean/UpdateMainResult")
    Call<Void> SubmitAudit(@Query("ResultID") int ResultID, @Query("Remarks") String Remarks);

    @GET("api/SixSLean/GetAuditSubmitted/{ID}")
    Call<List<ReportResponse>> FilterReport(@Path("ID") int ID, @Query("Option") int Option, @Query("AuditorID") int AuditorID);

    @POST("api/QualityBarcode/SaveDatsa")
    Call<Void> uploadImageModel(@Body List<Answer> answerModel);

    @POST("api/QualityBarcode/SaveDatsa")
    Call<Void> uploadImageMsodel(@Body List<Answer> answerModel);

    @POST("api/QualityBarcode/SaveDatsa")
    Observable<Void> uploadImageModelObservable(@Body List<Answer> answerModel);

    @Multipart
    @POST("api/QualityBarcode/SaveData")
    Call<List<Answer>> saveData(@Part("answerModel")  List<Answer> answerModel);

    @GET("umairmushtaq109/SixSApp/releases/download/v1.1.1/update-changelog.json")
    Call<UpdateInfo> getUpdateInfo();

//    @Multipart
//    @POST("api/QualityBarcode/SaveData")
//    Call<List<Answer>> saveData(@Part MultipartBody.Part filePart);

//    @GET("products/search")
//    Call<Product.ProductList> GetData(@Query("q") String param);
}

package com.example.newtest.api;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("eve/server_check.php")
    Call<ChargerList> checkServer();

    @GET("eve/company.php")
    Call<ChargerList> getCompany();

    @FormUrlEncoded
    @POST("eve/info.php")
    Call<ChargerList> getInfo(
            @Field("type[]") ArrayList<String> typeList,
            @Field("parking[]") ArrayList<String> parkingList,
            @Field("company[]") ArrayList<String> companyList
    );

    @FormUrlEncoded
    @POST("eve/info.php")
    Call<ChargerList> getAllStation(
            @Field("allStation") boolean allStation
    );

    @GET("portal/monitor/slist")
    Call<ChargerList> getStatus();

    @FormUrlEncoded
    @POST("eve/user_post.php")
    Call<Charger.ServerResponse> saveUser(
            @Field("id") String sid,
            @Field("nick") String nick,
            @Field("img") String img,
            @Field("time") String time
    );

    @FormUrlEncoded
    @POST("eve/bookmark.php")
    Call<ChargerList> getBookmark(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("eve/bookmark_my.php")
    Call<ChargerList> getMyBookmark(
            @Field("id") String id,
            @Field("orderAdd") boolean orderAdd,
            @Field("orderName") boolean orderName
    );

    @FormUrlEncoded
    @POST("eve/bookmark_post.php")
    Call<Charger.ServerResponse> saveBookmark(
            @Field("id") String id,
            @Field("sid") String sid
    );

    @FormUrlEncoded
    @POST("eve/bookmark_delete.php")
    Call<Charger.ServerResponse> deleteBookmark(
            @Field("id") String id,
            @Field("sid") String sid
    );

    @FormUrlEncoded
    @POST("eve/comment.php")
    Call<ChargerList> getComment(
            @Field("id") String id,
            @Field("sid") String sid,
            @Field("time") String time
    );

    @FormUrlEncoded
    @POST("eve/comment_my.php")
    Call<ChargerList> getCommentMy(
            @Field("id") String id
    );

    @Multipart
    @POST("eve/comment_post.php")
    Call<Charger.ServerResponse> postComment(
            @Part("id") RequestBody id,
            @Part("sid") RequestBody sid,
            @Part("content") RequestBody content,
            @Part("time") RequestBody time
    );

    @Multipart
    @POST("eve/comment_post.php")
    Call<Charger.ServerResponse> postComment(
            @Part("id") RequestBody id,
            @Part("sid") RequestBody sid,
            @Part("content") RequestBody content,
            @Part("time") RequestBody time,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("eve/comment_update.php")
    Call<Charger.ServerResponse> updateComment(
            @Part("id") RequestBody id,
            @Part("sid") RequestBody sid,
            @Part("content") RequestBody content,
            @Part("time") RequestBody time,
            @Part("delete") boolean delete
    );

    @Multipart
    @POST("eve/comment_update.php")
    Call<Charger.ServerResponse> updateComment(
            @Part("id") RequestBody id,
            @Part("sid") RequestBody sid,
            @Part("content") RequestBody content,
            @Part("time") RequestBody time,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST("eve/comment_delete.php")
    Call<Charger.ServerResponse> deleteComment(
            @Field("id") String id,
            @Field("time") String time
    );

//    @Headers({
//            "Authorization: KakaoAK 25fd7e9038027ae0adff4aac5e4d1a6e"
//    })
//    @POST("v2/user/me")
//    Call<Charger.Account> getAccount(
//            @Query("target_id_type") String target_id_type,
//            @Query("target_id") String target_id,
//            @Query("property_keys") String json
//    );
}
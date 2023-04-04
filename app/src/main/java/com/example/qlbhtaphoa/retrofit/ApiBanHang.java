package com.example.qlbhtaphoa.retrofit;



import com.example.qlbhtaphoa.model.LoaiSpModel;
import com.example.qlbhtaphoa.model.MessageModel;
import com.example.qlbhtaphoa.model.SanPhamMoiModel;
import com.example.qlbhtaphoa.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
   @GET("getloaisanpham.php")
    Observable<LoaiSpModel> getLoaiSanPham();

   @GET("getspmoi.php")
   Observable<SanPhamMoiModel> getSanPhamMoi();

   @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
   );
    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("mail") String mail,
            @Field("password") String password,
            @Field("username") String username,
            @Field("dienthoai") String dienthoai
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("mail") String mail,
            @Field("password") String password
    );
    @POST("themmoi.php")
    @FormUrlEncoded
    Observable<MessageModel> themsp(
            @Field("tensp") String tensp,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int id
    );
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
        @Field("id") int id
    );
 @POST("sua.php")
 @FormUrlEncoded
 Observable<MessageModel> suasp(
         @Field("tensp") String tensp,
         @Field("giasp") String giasp,
         @Field("hinhanh") String hinhanh,
         @Field("mota") String mota,
         @Field("loai") int idloai,
         @Field("id") int id
 );
 @POST("reset.php")
 @FormUrlEncoded
 Observable<UserModel> resetPass(
         @Field("mail") String mail

 );
}

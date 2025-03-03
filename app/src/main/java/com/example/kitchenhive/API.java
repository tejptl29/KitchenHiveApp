package com.example.kitchenhive;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

//    String BASE_URL = "http://192.168.0.65/product/app100/";

    String BASE_URL = "http://192.168.0.65:8000/";
    //String BASE_URL = "http://192.168.1.103:8000/";

    @Headers({
            "Accept: application/json",
            "Authorization: Bearer 1|hvkhYtKhRl7Lf68Zo6J21Qze7ov79RYWy2xGa9THaf14903b",
    })
    @POST("api/get_data/")
    @FormUrlEncoded
    Call<ResponseBody> get_dashboard_data(@Field("user_id") String user_id, @Field("veg") String veg, @Field("latitude") String latitude, @Field("longitude") String longitude);

    @Headers({
            "Accept: application/json",
            "Authorization: Bearer 1|hvkhYtKhRl7Lf68Zo6J21Qze7ov79RYWy2xGa9THaf14903b",
    })
    @POST("api/get_products/")
    @FormUrlEncoded
    Call<ResponseBody> get_products(@Field("user_id") String user_id,@Field("search") String search,@Field("cat_id") String cat_id,@Field("veg") String veg,@Field("latitude") String latitude, @Field("longitude") String longitude);

    @POST("api/get_stores_data/")
    @FormUrlEncoded
    Call<ResponseBody> get_stores_data(@Field("user_id") String user_id,@Field("store_id") String store_id,@Field("latitude") String latitude, @Field("longitude") String longitude);

    @POST("api/set_cancel_order/")
    @FormUrlEncoded
    Call<ResponseBody> set_cancel_order(@Field("user_id") String user_id,@Field("payment_id") String payment_id);


    @POST("api/register_user/")
    @FormUrlEncoded
    Call<ResponseBody> register_user(@Field("name") String name,@Field("email") String email,@Field("password") String password,@Field("phone") String phone);

    @POST("api/check_user_auth/")
    @FormUrlEncoded
    Call<ResponseBody> check_user_auth(@Field("email") String email,@Field("password") String password);

    @POST("api/get_single_product/")
    @FormUrlEncoded
    Call<ResponseBody> get_single_product(@Field("user_id") String user_id,@Field("product_id") String product_id);

    @POST("api/get_user_pro_fav/")
    @FormUrlEncoded
    Call<ResponseBody> get_pro_fav(@Field("user_id") String user_id);

    @POST("api/set_products_fav/")
    @FormUrlEncoded
    Call<ResponseBody> set_products_fav(@Field("user_id") String user_id ,@Field("product_id") String product_id, @Field("action") String action);

    @POST("api/add_user_cart/")
    @FormUrlEncoded
    Call<ResponseBody> add_user_cart(@Field("user_id") String user_id ,@Field("product_id") String product_id, @Field("quantity") String quantity, @Field("duration") String duration, @Field("interval") String interval, @Field("totalamount") String totalamount);

    @POST("api/get_cart_items/")
    @FormUrlEncoded
    Call<ResponseBody> get_cart_items(@Field("user_id") String user_id );

    @POST("api/remove_cart_items/")
    @FormUrlEncoded
    Call<ResponseBody> remove_cart_item(@Field("user_id") String user_id, @Field("cart_id") String cart_id );

    @POST("api/set_order/")
    @FormUrlEncoded
    Call<ResponseBody> set_order(@Field("user_id") String user_id,@Field("phone") String phone, @Field("email") String email, @Field("order_number") String order_number, @Field("description") String description, @Field("amount") String amount, @Field("items") String items);

    @POST("api/get_orders/")
    @FormUrlEncoded
    Call<ResponseBody> get_orders(@Field("user_id") String user_id,@Field("completed") String completed);

    @POST("api/get_user_subscriptions/")
    @FormUrlEncoded
    Call<ResponseBody> user_subscriptions(@Field("user_id") String user_id );

    @POST("api/forget_password/")
    @FormUrlEncoded
    Call<ResponseBody> user_forget_password(@Field("email") String email,@Field("password") String password);

//    @POST("user_request_app")
//    @FormUrlEncoded
//    Call<ResponseBody> user_request(@Field("api") String api, @Field("data") String data);
//
//    @POST("user_app")
//    @FormUrlEncoded
//    Call<ResponseBody> user_app(@Field("api") String api, @Field("data") String data);
//
//    @POST("dashboard_app")
//    @FormUrlEncoded
//    Call<ResponseBody> dashboard_app(@Field("api") String api, @Field("data") String data);
}

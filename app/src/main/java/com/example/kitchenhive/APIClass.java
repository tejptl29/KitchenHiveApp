package com.example.kitchenhive;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class APIClass {

    private JsonDataInterface jsonListener;

    void setOnJSONDataListener(JsonDataInterface listener)
    {
        jsonListener = listener;
    }

    interface JsonDataInterface
    {
        void onSuccess( String message, JSONObject json );
        void onFailure( String message );
    }

    /*void auth_app(HashMap<String, String> map, String api_name){

        JSONObject jsonObject = new JSONObject(map);
        String data = jsonObject.toString();
        System.out.println(data);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.auth_app(api_name, data);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1")){

                                if(new Utility().checkJSONDataNotNull(res, "data")){
                                    jsonListener.onSuccess(res.getString("message"), new JSONObject(res.getString("data").trim()));
                                }
                                else{
                                    jsonListener.onSuccess(res.getString("message"), new JSONObject(""));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                    System.out.println(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                            System.out.println("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                        System.out.println("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }*/

    /*void register_app(HashMap<String, String> map, String api_name){

        JSONObject jsonObject = new JSONObject(map);
        String data = jsonObject.toString();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.auth_app(api_name, data);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                    System.out.println(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                            System.out.println("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                        System.out.println("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void request_app(HashMap<String, String> map, String api_name){

        JSONObject jsonObject = new JSONObject(map);
        String data = jsonObject.toString();
        System.out.println(data);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.user_request(api_name, data);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    System.out.println(response.body());
                    System.out.println(response.message());
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                    System.out.println(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                            System.out.println("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                        System.out.println("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }*/

    void dashboard_data(String user_id, String veg, String latitude, String longitude){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_dashboard_data(user_id, veg, latitude,longitude);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    //System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        //System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        //System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }
    void set_cancel_order(String user_id, String store_order_item_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.set_cancel_order(user_id, store_order_item_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    //System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        //System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        //System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }
    void get_products(String user_id,String search,String cat_id,String veg, String latitude, String longitude){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_products(user_id,search,cat_id,veg, latitude ,longitude);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void get_stores_data(String user_id,String store_id, String latitude, String longitude){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_stores_data(user_id, store_id, latitude, longitude);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void set_user_cart(String user_id,String product_id,String quantity ,String duration,String interval,String totalamount){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.add_user_cart(user_id,product_id,quantity,duration,interval,totalamount);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void get_cart_items(String user_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_cart_items(user_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void delete_cart_items(String user_id,String cart_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.remove_cart_item(user_id, cart_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void set_order(String user_id, String phone, String email, String order_number,String description , String amount, String items){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.set_order(user_id,phone,email,order_number,description,amount,items);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void get_orders_data(String user_id,String completed){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_orders(user_id,completed);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void user_subscriptions(String user_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.user_subscriptions(user_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void user_forget_password(String email,String password){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.user_forget_password(email,password);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){

                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else {
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }



    void register_user(String name,String email, String password,String phone){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.register_user(name, email, password,phone);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess(res.getString("message").trim(), new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void check_user_auth(String email, String password){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.check_user_auth(email, password);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void get_single_product(String user_id, String product_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_single_product(user_id, product_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void get_pro_fav(String user_id){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.get_pro_fav(user_id);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }

    void set_products_fav(String user_id,String product_id , String action){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.set_products_fav(user_id, product_id, action);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1") || res.getString("status").trim().equals("true")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }


    /*void user_app(HashMap<String, String> map, String api_name){

        JSONObject jsonObject = new JSONObject(map);
        String data = jsonObject.toString();
        System.out.println(data);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> apiCall = api.user_app(api_name, data);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try{
                    if(response.body() != null){
                        System.out.println(response.body());
                        JSONObject res = new JSONObject(response.body().string());
                        if(res.has("status") && !res.isNull("status") && !res.getString("status").trim().isEmpty()){
                            if(res.getString("status").trim().equals("1")){
                                if(res.has("data") && !res.isNull("data") && !res.getString("data").trim().isEmpty()){
                                    jsonListener.onSuccess("Success API Call.", new JSONObject(res.getString("data").trim()));
                                }else
                                    jsonListener.onFailure("Empty server data response. Please try again.");
                            }else{
                                if(res.has("message") && !res.isNull("message") && !res.getString("message").trim().isEmpty()){
                                    jsonListener.onFailure(res.getString("message").trim());
                                    System.out.println(res.getString("message").trim());
                                }else{
                                    jsonListener.onFailure("Invalid server response. Please try again.");
                                }
                            }
                        }else{
                            jsonListener.onFailure("Invalid server response. Please try again.");
                            System.out.println("Invalid server response. Please try again.");
                        }
                    }else{
                        jsonListener.onFailure("Server did not respond. Please try again.");
                        System.out.println("Server did not respond. Please try again.");
                    }
                }catch (JSONException e){
                    jsonListener.onFailure("JSON Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }catch (IOException e){
                    jsonListener.onFailure("I/O Exception.");
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    jsonListener.onFailure("Connection timeout.");
                    System.out.println(t.getLocalizedMessage());
                }else{
                    if(call.isCanceled()){
                        jsonListener.onFailure("Forcefully cancel request.");
                        System.out.println(t.getLocalizedMessage());
                    }else{
                        jsonListener.onFailure("Internal server error. Please try again.");
                        System.out.println(t.getLocalizedMessage());
                    }
                }
            }
        });
    }*/
}


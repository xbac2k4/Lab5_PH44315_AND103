package com.example.lab5_and103.service;

import com.example.lab5_and103.model.Distributor;
import com.example.lab5_and103.model.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
//    10.24.11.254 192.168.1.118 10.24.24.76
    String ipv4 = "192.168.1.118";
    String DOMAIN = "http://"+ ipv4 +":3000/api/";

    @GET("get-list")
    Call<Response<ArrayList<Distributor>>> getDistributor();

    @GET("search-distributor")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);

    @POST("add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);

    @PUT("update-distributor/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id") String id, @Body Distributor distributor);

    @DELETE("delete-distributor/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id") String id);

}

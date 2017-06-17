package com.jakfarshodiq.jakfar.antriansimapp.rest;

/**
 * Created by JAKFAR on 6/2/17.
 */

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface PostKodeAntrian {
    @FormUrlEncoded
    @POST("service_get_data_sim.php")
    Call<ResponseBody> postMessage(@FieldMap HashMap<String, String> params);
//    Call<ResponseBody> postMessage();
}
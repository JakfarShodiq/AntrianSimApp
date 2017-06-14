package com.jakfarshodiq.jakfar.antriansimapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakfarshodiq.jakfar.antriansimapp.models.RequestKodeAntrian;
import com.jakfarshodiq.jakfar.antriansimapp.rest.PostKodeAntrian;

import java.util.HashMap;

import okhttp3.ResponseBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import com.jakfarshodiq.jakfar.antriansimapp.DetailsActivity;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Retrofit twohRetro;

    public static final String ROOT_URL = "https://api.myjson.com/bins/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeRetrofit();
        postMessage();
    }

    private void initializeRetrofit(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        twohRetro = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void postMessage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "elisabeth");
        params.put("message", "Hey, what are you doing?");
        params.put("sex", "female");
        params.put("age", "21");

        PostKodeAntrian apiService = twohRetro.create(PostKodeAntrian.class);
        Call<ResponseBody> result = apiService.postMessage();
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body()!=null) {
//                        String body = response.body();
                        Gson gson = new Gson();
                        RequestKodeAntrian models = gson.fromJson(response.body().string(), RequestKodeAntrian.class);

                        Toast.makeText(MainActivity.this, " response message " + models.getIdPenduduk(), Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, " response message null", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

package com.jakfarshodiq.jakfar.antriansimapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.*;

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

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Retrofit twohRetro;

    public static final String ROOT_URL = "http://robotijo.esy.es/";

    EditText idAntrian;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeRetrofit();

        idAntrian = (EditText) findViewById(R.id.idAntrian);

        //idAntrian.setText("JUNI100011");

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_antrian = idAntrian.getText().toString();
                if (!id_antrian.isEmpty()) {
                    postMessage();
                } else {
                    Toast.makeText(MainActivity.this, "Kode Antrian tidak bolek kosong!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        params.put("id_antrian", idAntrian.getText().toString());

        PostKodeAntrian apiService = twohRetro.create(PostKodeAntrian.class);
        Call<ResponseBody> result = apiService.postMessage(params);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body()!=null) {
//                        String body = response.body();
                        Gson gson = new Gson();
                        RequestKodeAntrian models = gson.fromJson(response.body().string(), RequestKodeAntrian.class);

                        String status = models.getStatus();
                        String status_antrian = models.getStatusAntrian();
                        String id_antrian = models.getIdAntrian();

                        if (status.equals("Gagal")){
                            Toast.makeText(MainActivity.this, "Kode Antrian "+ id_antrian +" tidak ada dalam sistem! ", Toast.LENGTH_LONG).show();
                        } else {
                            if (status_antrian.equals("finish")){
                                Toast.makeText(MainActivity.this, "Kode Antrian "+ id_antrian +" sudah selesai.", Toast.LENGTH_LONG).show();
                            } else {
                                Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                                i.putExtra("id_antrian", id_antrian);
                                startActivity(i);
                            }
                        }
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

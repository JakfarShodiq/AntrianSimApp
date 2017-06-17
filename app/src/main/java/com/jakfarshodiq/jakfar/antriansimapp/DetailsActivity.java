package com.jakfarshodiq.jakfar.antriansimapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.*;

import com.google.gson.Gson;
import com.jakfarshodiq.jakfar.antriansimapp.models.RequestKodeAntrian;
import com.jakfarshodiq.jakfar.antriansimapp.rest.PostKodeAntrian;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    private Retrofit retrofit;

    private TextView nama;
    private TextView ktp;
    private TextView jenisSim;
    private ImageView simImage;
    private TextView antrianBerjalan;
    private TextView antrianAsli;
    private Button btnRefresh;

    public static final String ROOT_URL = "http://robotijo.esy.es/";

    String id_antrian;
    String btnRefreshStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();

        id_antrian = extras.getString("id_antrian");

        nama = (TextView) findViewById(R.id.nama);
        ktp = (TextView) findViewById(R.id.ktp);
        jenisSim = (TextView) findViewById(R.id.jenisSim);
        simImage = (ImageView) findViewById(R.id.simImage);
        antrianBerjalan = (TextView) findViewById(R.id.antrianBerjalan);
        antrianAsli = (TextView) findViewById(R.id.antrianAsli);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        initializeRetrofit();
        postMessage();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefreshStatus = "YES";
                postMessage();
            }
        });

        // Refresh every x minutes
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                postMessage();
                ha.postDelayed(this, 1000 * 60 * 2);
            }
        }, 1000 * 60 * 2);
    }

    private void initializeRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void postMessage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id_antrian", id_antrian);

        final ProgressDialog loading = ProgressDialog.show(this, "Mengambil data dari server", "Mohon tunggu ..", false, false);

        PostKodeAntrian apiService = retrofit.create(PostKodeAntrian.class);
        Call<ResponseBody> result = apiService.postMessage(params);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loading.dismiss();
                    if (response.body() != null) {
//                        String body = response.body();
                        Gson gson = new Gson();
                        RequestKodeAntrian models = gson.fromJson(response.body().string(), RequestKodeAntrian.class);

                        String status = models.getStatus();
                        String jenisSimStr = models.getJenisSim();
                        String antrianBerjalanStr = models.getNoAntrianBerjalan();
                        String antrianNoStr = models.getNoAntrian();

                        String status_antrian = models.getStatusAntrian();
                        if (status_antrian.equals("finish")) {
                            Toast.makeText(DetailsActivity.this, "Kode Antrian "+ id_antrian +" sudah selesai.", Toast.LENGTH_LONG).show();
                        } else {
                            if (status.equals("Sukses")) {
                                nama.setText(" : " + models.getNamaPenduduk());
                                ktp.setText(" : " + models.getIdPenduduk());
                                jenisSim.setText(models.getJenisSim());

                                if (jenisSimStr.matches("(.*)SIM C")) {
                                    simImage.setImageResource(R.drawable.motorbiking);
                                } else if (jenisSimStr.matches("(.*)SIM A")) {
                                    simImage.setImageResource(R.drawable.car);
                                } else if (jenisSimStr.matches("(.*)SIM B")) {
                                    simImage.setImageResource(R.drawable.bus);
                                }

                                antrianBerjalan.setText(antrianBerjalanStr);
                                antrianAsli.setText(models.getNoAntrian());

                                int sisaAntrian = Integer.parseInt(antrianBerjalanStr);
                                int noAntrian = Integer.parseInt(antrianNoStr);
                                int selisih = noAntrian - sisaAntrian;

                                if (selisih < 5 && selisih > 0) {
                                    if (!"YES".equals(btnRefreshStatus)) {
                                        notifyUser(selisih);
                                    }
                                } else if (selisih == noAntrian) {
                                    Toast.makeText(DetailsActivity.this, "Kode Antrian "+ id_antrian +" sedang berlangsung", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void notifyUser(int sisaAntrian) {
        // prepare intent which is triggered if the
        // notification is selected

        Intent intent = new Intent(this, DetailsActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(this)
                .setContentTitle("Antrian Sim App Notification")
                .setContentText("Antrian anda kurang " + sisaAntrian + " lagi lhoo..")
                .setSmallIcon(R.drawable.cards)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
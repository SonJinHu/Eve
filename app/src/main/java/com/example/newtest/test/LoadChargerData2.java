package com.example.newtest.test;

import androidx.annotation.NonNull;

import com.example.newtest.api.ApiService;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.api.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadChargerData2 {
    public static void enqueue(final ChargerDataCallback callback) {
        ApiService api = RetroClient.getEvApiService();
        Call<ChargerList> call = api.getStatus();
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Status> items = response.body().getStatus();
                        callback.onResponse(items);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {
                callback.onFailure();
            }
        });
    }

    interface ChargerDataCallback {
        void onResponse(ArrayList<Charger.Status> items);

        void onFailure();
    }
}

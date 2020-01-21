package com.example.newtest.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.newtest.api.ApiService;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.api.RetroClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Server {

    private Context context;

    public Server(Context context) {
        this.context = context;
    }

    public void checkServer(final OnCheckServerTaskListener listener) {
        listener.onPreTask();
        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.checkServer();
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String updatedDate = response.body().getDate().get(0).getDate();
                        String savedDate = Shared.getUpdatedDate(context);

                        if (savedDate == null) {
                            Shared.setUpdatedDate(context, updatedDate);
                            listener.onPostTask(true);
                            Log.e("Server", "check server: must load data");
                            return;
                        }

                        if (!savedDate.equals(updatedDate)) {
                            Shared.setUpdatedDate(context, updatedDate);
                            listener.onPostTask(true);
                            Log.e("Server", "check server: must load data");
                        } else {
                            listener.onPostTask(false);
                            Log.e("Server", "check server: you don't have to load data");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void loadCompany(final OnPostTaskListener listener) {
        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getCompany();
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Company> newCompanyList = response.body().getCompany();
                        ArrayList<Charger.Company> savedCompanyList = Shared.getCompanyList(context);

                        if (savedCompanyList == null) {
                            Shared.setCompanyList(context, newCompanyList);
                            Shared.initTypeCheck(context);
                            Shared.initParkingCheck(context);
                            Shared.initCompanyCheck(context, newCompanyList.size());
                        } else {
                            if (savedCompanyList.size() != newCompanyList.size()) {
                                Shared.setCompanyList(context, newCompanyList);
                                Shared.initCompanyCheck(context, newCompanyList.size());
                            }
                        }
                        listener.onPostTask();
                        Log.e("Server", "load company: " + newCompanyList.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    public void loadAllStation(final OnPostTaskListener listener) {
        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getAllStation(true);
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Info> items = response.body().getInfo();
                        Shared.setInfoList(context, items);
                        listener.onPostTask();
                        Log.e("Server", "load all station: " + items.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void loadStatus(final OnTaskListener listener) {
        listener.onPreTask();
        ApiService api = RetroClient.getEvApiService();
        Call<ChargerList> call = api.getStatus();
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Status> items = response.body().getStatus();
                        Shared.setStatus(context, items);
                        listener.onPostTask();
                        Log.e("Server", "load status: " + items.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    public void loadStation(final OnTaskListener listener) {
        listener.onPreTask();
        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getInfo(getTypeParameter(), getParkingParameter(), getCompanyParameter());
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Info> items = response.body().getInfo();
                        Shared.setInfoList(context, items);
                        listener.onPostTask();
                        Log.e("Server", "load station: " + items.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void loadComment(String id, String sid, String time, final OnCommentTaskListener listener) {
        listener.onPreTask();
        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getComment(id, sid, time);
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        listener.onPostTask(response);
                        Log.e("Server", "load comment: success / " + response.body().getComment().size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    public void saveUser() {
        HashMap<String, String> map = Shared.getAccount(context);
        String id = map.get("id");
        String nick = map.get("nick");
        String img = map.get("img");
        String time = TimeCalculator.getCurrentTime();

        ApiService api = RetroClient.getApiService();
        Call<Charger.ServerResponse> call = api.saveUser(id, nick, img, time);
        call.enqueue(new Callback<Charger.ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getInsertResult()) {
                            Log.e("Server", "saveUser: success");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Charger.ServerResponse> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void loadBookmark(final OnPostTaskListener listener) {
        HashMap<String, String> map = Shared.getAccount(context);
        String id = map.get("id");

        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getBookmark(id);
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Bookmark> items = response.body().getBookmark();
                        Shared.setBookmark(context, items);
                        listener.onPostTask();
                        Log.e("Server", "load bookmark : success / " + response.body().getBookmark().size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    public void saveBookmark(final String sid) {
        HashMap<String, String> map = Shared.getAccount(context);
        String id = map.get("id");

        ApiService api = RetroClient.getApiService();
        Call<Charger.ServerResponse> call = api.saveBookmark(id, sid);
        call.enqueue(new Callback<Charger.ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getInsertResult()) {
                            Charger.Bookmark item = new Charger.Bookmark();
                            item.setSid(sid);

                            ArrayList<Charger.Bookmark> items = Shared.getBookmark(context);
                            items.add(item);
                            Shared.setBookmark(context, items);
                            Toast.makeText(context, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("Server", "saveBookmark: success");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Charger.ServerResponse> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    public void deleteBookmark(final String sid) {
        HashMap<String, String> map = Shared.getAccount(context);
        String id = map.get("id");

        ApiService api = RetroClient.getApiService();
        Call<Charger.ServerResponse> call = api.deleteBookmark(id, sid);
        call.enqueue(new Callback<Charger.ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getInsertResult()) {
                            ArrayList<Charger.Bookmark> items = Shared.getBookmark(context);
                            for (Charger.Bookmark item : items) {
                                if (sid.equals(item.getSid())) {
                                    items.remove(item);
                                    break;
                                }
                            }
                            Shared.setBookmark(context, items);
                            Toast.makeText(context, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.e("Server", "deleteBookmark: success");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Charger.ServerResponse> call, @NonNull Throwable t) {
                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private ArrayList<String> getTypeParameter() {
        ArrayList<Boolean> checkList = Shared.getTypeCheck(context);
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<String> resultList = new ArrayList<>();

        if (checkList.get(0)) {
            dataList.add("3");
            dataList.add("6");
            dataList.add("7");
        }

        if (checkList.get(1)) {
            dataList.add("1");
            dataList.add("3");
            dataList.add("5");
            dataList.add("6");
        }

        if (checkList.get(2)) {
            dataList.add("4");
            dataList.add("5");
            dataList.add("6");
        }

        if (checkList.get(3)) {
            dataList.add("2");
        }

        if (checkList.get(5)) {
            dataList.add("89");
        }

        // 중복데이터 제거
        for (int i = 0; i < dataList.size(); i++) {
            if (!resultList.contains(dataList.get(i))) {
                resultList.add(dataList.get(i));
            }
        }
        return resultList;
    }

    private ArrayList<String> getParkingParameter() {
        ArrayList<Boolean> checkList = Shared.getParkingCheck(context);
        ArrayList<String> dataList = new ArrayList<>();
        ArrayList<String> resultList = new ArrayList<>();

        if (checkList.get(0)) { // 모두
            dataList.add("0");
            dataList.add("1");
        }

        if (checkList.get(1)) { // 유료
            dataList.add("1");
        }

        if (checkList.get(2)) { // 무료
            dataList.add("0");
        }

        // 중복데이터 제거
        for (int i = 0; i < dataList.size(); i++) {
            if (!resultList.contains(dataList.get(i))) {
                resultList.add(dataList.get(i));
            }
        }
        return resultList;
    }

    private ArrayList<String> getCompanyParameter() {
        ArrayList<Charger.Company> companyList = Shared.getCompanyList(context);
        ArrayList<Boolean> checkList = Shared.getCompanyCheck(context);
        ArrayList<String> resultList = new ArrayList<>();

        for (int i = 0; i < companyList.size(); i++) {
            if (!checkList.get(i))
                continue;
            resultList.add(companyList.get(i).getChgeMange());
        }
        return resultList;
    }

    public interface OnTaskListener {
        void onPreTask();

        void onPostTask();
    }

    public interface OnPostTaskListener {
        void onPostTask();
    }

    public interface OnCheckServerTaskListener {
        void onPreTask();

        void onPostTask(boolean beLoadData);
    }

    public interface OnCommentTaskListener {
        void onPreTask();

        void onPostTask(Response<ChargerList> response);
    }
}

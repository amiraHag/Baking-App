package com.example.android.baking.data.api;

import com.example.android.baking.BuildConfig;
import com.example.android.baking.utils.AppHelper;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static APIClient ourInstance = new APIClient();
    private static APIInterfaceService apiInterfaceService;
    private  static Retrofit API_Retrofit = null;


    public static APIClient getInstance() {
        return ourInstance;
    }

    private APIClient() {

        if(API_Retrofit == null){
            setNewBaseURL();
        }

        apiInterfaceService = API_Retrofit.create(APIInterfaceService.class);
    }

    public static APIInterfaceService getAPIInterfaceService() {
        return apiInterfaceService;
    }
    public void setNewBaseURL(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel((BuildConfig.DEBUG)
                        ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();

        API_Retrofit = new Retrofit.Builder()
                .baseUrl(AppHelper.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

    }

}

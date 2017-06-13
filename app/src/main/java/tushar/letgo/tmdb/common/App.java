package tushar.letgo.tmdb.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.dipendency.AppComponent;
import tushar.letgo.tmdb.dipendency.AppModule;
import tushar.letgo.tmdb.dipendency.DaggerAppComponent;

/**
 * Created by Tushar on 6/12/17.
 */

public class App extends Application {

    public static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private AppComponent appComponent;

    private OkHttpClient okHttpClient;

    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static App get(Activity activity) {
        return (App) activity.getApplication();
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public OkHttpClient provideOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpClientBuilder.addInterceptor(loggingInterceptor);

            okhttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
            okhttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
            okhttpClientBuilder.writeTimeout(10, TimeUnit.SECONDS);

            File cacheDir = new File(getCacheDir(), "http");
            Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
            okhttpClientBuilder.cache(cache);
            okHttpClient = okhttpClientBuilder.build();
        }
        return okHttpClient;
    }

    private Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    private Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public ApiService getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit(ApiService.URL).create(ApiService.class);
        }
        return apiService;
    }
}

package tushar.letgo.tmdb.dipendency;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.common.App;

/**
 * Created by Tushar on 6/12/17.
 */

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App provideApp() {
        return (App) application;
    }

    @Provides
    @Singleton
    ApiService provideApiService(App app) {
        return app.getApiService();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(App app) {
        return app.provideOkHttpClient();
    }
}

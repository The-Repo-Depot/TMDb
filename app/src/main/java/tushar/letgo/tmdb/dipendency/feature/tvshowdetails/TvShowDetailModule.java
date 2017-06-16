package tushar.letgo.tmdb.dipendency.feature.tvshowdetails;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.feature.tvshowdetails.TvShowDetailPresenter;

/**
 * Created by Tushar on 6/13/17.
 */

@Module
public class TvShowDetailModule {
    @Provides
    @Singleton
    public TvShowDetailPresenter provideTvShowDetailPresenter(ApiService apiService) {
        return new TvShowDetailPresenter(apiService);
    }
}

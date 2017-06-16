package tushar.letgo.tmdb.dipendency.feature.tvshowlisting;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.feature.tvshowlisting.TvShowListingPresenter;

/**
 * Created by Tushar on 6/13/17.
 */

@Module
public class TvShowListingModule {
    @Provides
    @Singleton
    public TvShowListingPresenter provideTvShowListingPresenter(ApiService apiService) {
        return new TvShowListingPresenter(apiService);
    }
}

package tushar.letgo.tmdb.dipendency;

import javax.inject.Singleton;

import dagger.Component;
import tushar.letgo.tmdb.common.GlideSetting;
import tushar.letgo.tmdb.dipendency.feature.tvshowdetails.TvShowDetailComponent;
import tushar.letgo.tmdb.dipendency.feature.tvshowdetails.TvShowDetailModule;
import tushar.letgo.tmdb.dipendency.feature.tvshowlisting.TvShowListingComponent;
import tushar.letgo.tmdb.dipendency.feature.tvshowlisting.TvShowListingModule;

/**
 * Created by Tushar on 6/12/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(GlideSetting glideSetting);

    TvShowListingComponent plus(TvShowListingModule homeModule);

    TvShowDetailComponent plus(TvShowDetailModule homeModule);
}

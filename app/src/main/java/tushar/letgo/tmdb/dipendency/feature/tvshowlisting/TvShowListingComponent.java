package tushar.letgo.tmdb.dipendency.feature.tvshowlisting;

import javax.inject.Singleton;

import dagger.Subcomponent;
import tushar.letgo.tmdb.feature.tvshowlisting.TvShowListingFragment;

/**
 * Created by Tushar on 6/13/17.
 */
@Singleton
@Subcomponent(modules = TvShowListingModule.class)
public interface TvShowListingComponent {
    void inject(TvShowListingFragment fragment);
}

package tushar.letgo.tmdb.dipendency.feature.tvshowdetails;

import javax.inject.Singleton;

import dagger.Subcomponent;
import tushar.letgo.tmdb.feature.tvshowdetails.TvShowDetailFragment;

/**
 * Created by Tushar on 6/13/17.
 */
@Singleton
@Subcomponent(modules = TvShowDetailModule.class)
public interface TvShowDetailComponent {
    void inject(TvShowDetailFragment tvShowDetailFragment);
}

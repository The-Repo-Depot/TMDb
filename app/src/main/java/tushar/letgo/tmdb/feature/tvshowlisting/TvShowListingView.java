package tushar.letgo.tmdb.feature.tvshowlisting;

import java.util.List;

import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/13/17.
 */

public interface TvShowListingView {
    void showProgress();

    void hideProgress();

    void showTvShowsLoadingProgress();

    void hideTvShowsLoadingProgress();

    void showTvShows(List<TvShow> tvShows);

    void hideErrors();

    void showTvShowLoadingError(String reason);

    void showTvShowLoadingError();

    boolean isRefreshing();

    void hideRefreshing();
}

package tushar.letgo.tmdb.feature.tvshowdetails;

import java.util.List;

import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/14/17.
 */

interface TvShowDetailView {
    void showProgress();

    void hideProgressWithError(String reason);

    void hideProgress();

    void showSimilarShows(List<TvShow> similarTvShows);

    long getUserSelectedTvShowId();
}

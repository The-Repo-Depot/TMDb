package tushar.letgo.tmdb.feature.tvshowlisting;

import java.util.List;

import tushar.letgo.tmdb.model.TvShow;

public interface LoadTvShowListener {
    void onTvShowLoadingStart();

    void onTvShowLoadingSuccess(List<TvShow> tvShows);

    void onTvShowLoadingFailure(String reason);
}
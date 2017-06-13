package tushar.letgo.tmdb.feature.tvshowlisting;

import java.util.List;

import tushar.letgo.tmdb.model.TvShow;

public interface RefreshTvShowListener {
    void onRefreshStart();

    void onRefreshSuccess(List<TvShow> tvShows);

    void onRefreshFailure(String reason);
}
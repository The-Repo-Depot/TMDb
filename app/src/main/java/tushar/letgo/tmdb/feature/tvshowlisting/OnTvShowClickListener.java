package tushar.letgo.tmdb.feature.tvshowlisting;

import android.view.View;

import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/13/17.
 */

public interface OnTvShowClickListener {
    void onTvShowSelected(View selectedView, TvShow tvShow, int position);
}

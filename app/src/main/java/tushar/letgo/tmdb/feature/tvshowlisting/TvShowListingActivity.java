package tushar.letgo.tmdb.feature.tvshowlisting;

import android.os.Bundle;

import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.PresenterActivity;

public class TvShowListingActivity extends PresenterActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tv_show_listing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root, TvShowListingFragment.newInstance(), TvShowListingFragment.TAG)
                    .commit();
        }
    }
}

package tushar.letgo.tmdb.feature.tvshowlisting;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.Bind;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.PresenterActivity;

public class TvShowListingActivity extends PresenterActivity implements TvShowListingView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.tv_list)
    RecyclerView tvList;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.view_error_no_content)
    View errorNoContentView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tv_show_listing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onRefresh() {

    }
}

package tushar.letgo.tmdb.feature.tvshowlisting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.BasePresenterFragment;
import tushar.letgo.tmdb.common.view.EndlessRecyclerOnScrollListener;
import tushar.letgo.tmdb.dipendency.feature.tvshowlisting.TvShowListingModule;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/13/17.
 */

public class TvShowListingFragment extends BasePresenterFragment<TvShowListingView, TvShowListingPresenter>
        implements TvShowListingView,
        SwipeRefreshLayout.OnRefreshListener,
        OnTvShowClickListener {

    public static final String TAG = TvShowListingFragment.class.getName();

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.tv_list)
    RecyclerView mTvShowRecyclerView;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.view_error_no_content)
    View mErrorNoContentView;

    private GridLayoutManager mGridLayoutManager;

    private TvShowRecyclerViewAdapter mTvShowRecyclerViewAdapter;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    private EndlessRecyclerOnScrollListener.State state = new EndlessRecyclerOnScrollListener.State();

    List<TvShow> tvShows = new ArrayList<>();

    @Inject
    TvShowListingPresenter presenter;

    public static Fragment newInstance() {
        return new TvShowListingFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tv_show_listing;
    }

    @NonNull
    @Override
    public TvShowListingPresenter onCreatePresenter() {
        app.getAppComponent()
                .plus(new TvShowListingModule())
                .inject(this);
        return presenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mTvShowRecyclerViewAdapter = new TvShowRecyclerViewAdapter(getActivity(), tvShows, this);

        mTvShowRecyclerView.setAdapter(mTvShowRecyclerViewAdapter);
        mGridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.tv_show_columns));
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mTvShowRecyclerViewAdapter.getItemViewType(position);

                switch (itemViewType) {
                    case TvShowRecyclerViewAdapter.VIEW_TYPE_ITEM:
                        return 1;
                    case TvShowRecyclerViewAdapter.VIEW_TYPE_PROGRESS:
                        return getResources().getInteger(R.integer.tv_show_columns);
                    default:
                        return -1;
                }
            }
        });
        mTvShowRecyclerView.setLayoutManager(mGridLayoutManager);

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mTvShowRecyclerView) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                return true;
            }

            @Override
            public void enableRefresh() {
                mSwipeRefreshLayout.setEnabled(true);
            }

            @Override
            public void disableRefresh() {
                mSwipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onScroll() {
                // skip
            }
        };

        mEndlessRecyclerOnScrollListener.setState(state);
        mTvShowRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTvShowSelected(View selectedView, TvShow tvShow) {

    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showTvShowsLoadingProgress() {
        mTvShowRecyclerViewAdapter.add(ProgressTvShow.INSTANCE);
    }

    @Override
    public void hideTvShowsLoadingProgress() {
        mTvShowRecyclerViewAdapter.remove(ProgressTvShow.INSTANCE);
    }

    @Override
    public void showTvShows(List<TvShow> tvShows) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvShowRecyclerViewAdapter.addAll(tvShows);
            }
        });
    }

    @Override
    public void hideErrors() {
        mErrorNoContentView.setVisibility(View.GONE);
    }

    @Override
    public void showTvShowLoadingError(String reason) {
        // skip
    }

    @Override
    public void showTvShowLoadingError() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mErrorNoContentView.setVisibility(View.VISIBLE);
            }
        });
    }
}

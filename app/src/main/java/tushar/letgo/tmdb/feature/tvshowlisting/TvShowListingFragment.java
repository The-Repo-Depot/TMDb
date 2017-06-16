package tushar.letgo.tmdb.feature.tvshowlisting;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.BasePresenterFragment;
import tushar.letgo.tmdb.common.view.DaddySwipeRefreshLayout;
import tushar.letgo.tmdb.common.view.EndlessRecyclerOnScrollListener;
import tushar.letgo.tmdb.dipendency.feature.tvshowlisting.TvShowListingModule;
import tushar.letgo.tmdb.feature.tvshowdetails.TvShowDetailActivity;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/13/17.
 */

public class TvShowListingFragment extends BasePresenterFragment<TvShowListingView, TvShowListingPresenter>
        implements TvShowListingView,
        SwipeRefreshLayout.OnRefreshListener,
        OnTvShowClickListener {

    public static final String TAG = TvShowListingFragment.class.getName();

    private static final String STATE_ERROR = "state_error";
    private static final String STATE_LOADING = "state_loading";
    private static final String STATE_TV_SHOWS = "state_shows";
    private static final String STATE_SELECTED_POSITION = "state_selected_position";

    @Bind(R.id.swipe_refresh_layout)
    DaddySwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.tv_list)
    RecyclerView mTvShowRecyclerView;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.layout_error)
    LinearLayout errorLayout;

    private GridLayoutManager mGridLayoutManager;

    private TvShowRecyclerViewAdapter mTvShowRecyclerViewAdapter;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    private EndlessRecyclerOnScrollListener.State state = new EndlessRecyclerOnScrollListener.State();

    List<TvShow> tvShows = new ArrayList<>();

    int mSelectedPosition = -1;

    private boolean isError;

    private boolean isLoading;

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTvShowRecyclerViewAdapter != null) {
            outState.putBoolean(STATE_LOADING, isLoading);
            outState.putBoolean(STATE_ERROR, isError);
            outState.putParcelable(STATE_TV_SHOWS, Parcels.wrap(mTvShowRecyclerViewAdapter.getAllItems()));
            outState.putInt(STATE_SELECTED_POSITION, mSelectedPosition);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            if (!savedInstanceState.getBoolean(STATE_LOADING)
                    && !savedInstanceState.getBoolean(STATE_ERROR)) {
                tvShows = Parcels.unwrap(savedInstanceState.getParcelable(STATE_TV_SHOWS));
                mSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, -1);
            } else if (savedInstanceState.getBoolean(STATE_LOADING)) {
                showProgress();
            } else {
                showTvShowLoadingError();
            }
        }

        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setCanChildScrollUpCallback(new DaddySwipeRefreshLayout.ScrollUpApplicableListener() {
            @Override
            public boolean isScrollUpApplicable() {
                return mTvShowRecyclerView != null && ViewCompat.canScrollVertically(mTvShowRecyclerView, -1);
            }
        });
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
                getPresenter().loadMore();
                return true;
            }

            @Override
            public void enableRefresh() {
                if (!mSwipeRefreshLayout.isEnabled()) {
                    mSwipeRefreshLayout.setEnabled(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void disableRefresh() {
                if (mSwipeRefreshLayout.isEnabled()) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }

            @Override
            public void onScroll() {
                // skip
            }
        };

        mEndlessRecyclerOnScrollListener.setState(state);
        mTvShowRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        if (mSelectedPosition != -1) {
            mTvShowRecyclerView.scrollToPosition(mSelectedPosition);
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().reload();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTvShowSelected(View selectedView, TvShow tvShow, int position) {
        Timber.d(tvShow.getTitle() + " " + tvShow.getId());
        mSelectedPosition = position;
        TvShowDetailActivity.open(getActivity(), tvShow);
    }

    @Override
    public void showProgress() {
        isLoading = true;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void hideProgress() {
        isLoading = false;
        getActivity().runOnUiThread(() -> {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        });
    }

    @Override
    public void showTvShowsLoadingProgress() {
        mTvShowRecyclerView.post(() -> mTvShowRecyclerViewAdapter.add(ProgressTvShow.INSTANCE));
    }

    @Override
    public void hideTvShowsLoadingProgress() {
        mTvShowRecyclerView.post(() -> mTvShowRecyclerViewAdapter.remove(ProgressTvShow.INSTANCE));
    }

    @Override
    public void showTvShows(List<TvShow> tvShows) {
        isError = false;
        mTvShowRecyclerView.post(() -> mTvShowRecyclerViewAdapter.addAll(tvShows));
    }

    @Override
    public void hideErrors() {
        errorLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTvShowLoadingError(String reason) {
        // skip
    }

    @Override
    public void showTvShowLoadingError() {
        isError = true;
        getActivity().runOnUiThread(() -> errorLayout.setVisibility(View.VISIBLE));
    }

    @Override
    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void hideRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.layout_error)
    void onClickError() {
        getPresenter().reloadWhenError();
    }
}

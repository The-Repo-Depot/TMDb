package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.f2prateek.dart.InjectExtra;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import timber.log.Timber;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.BasePresenterFragment;
import tushar.letgo.tmdb.dipendency.feature.tvshowdetails.TvShowDetailModule;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/14/17.
 */

public class TvShowDetailFragment extends BasePresenterFragment<TvShowDetailView, TvShowDetailPresenter> implements TvShowDetailView {
    public static final String TAG = TvShowDetailFragment.class.getName();

    private static final String STATE_SELECTED_TV_SHOW = "selected_tv_show";
    private static final String STATE_TV_SHOWS = "state_shows";
    private static final String STATE_SELECTED_POSITION = "state_selected_position";

    @InjectExtra(STATE_SELECTED_TV_SHOW)
    TvShow selectedTvShow;

    @Inject
    TvShowDetailPresenter presenter;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.tv_show_view_pager)
    ViewPager tvShowViewPager;

    private List<TvShow> tvShows = new ArrayList<>();

    private ScaleResponsiblePagerAdapter scaleResponsiblePagerAdapter;

    private int mSelectedPosition = -1;

    public static TvShowDetailFragment newInstance(TvShow tvShow) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(STATE_SELECTED_TV_SHOW, Parcels.wrap(tvShow));

        TvShowDetailFragment tvShowDetailFragment = new TvShowDetailFragment();
        tvShowDetailFragment.setArguments(arguments);
        return tvShowDetailFragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tv_show_details;
    }

    @NonNull
    @Override
    public TvShowDetailPresenter onCreatePresenter() {
        app.getAppComponent()
                .plus(new TvShowDetailModule())
                .inject(this);
        return presenter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvShows.size() > 0) {
            outState.putParcelable(STATE_TV_SHOWS, Parcels.wrap(tvShows));
            Timber.tag("save instance state").d("tv show size %d", tvShows.size());
        }
        if (scaleResponsiblePagerAdapter != null && scaleResponsiblePagerAdapter.getCount() > 0) {
            outState.putInt(STATE_SELECTED_POSITION, tvShowViewPager.getCurrentItem());
            Timber.tag("save instance state").d("current position %d", tvShowViewPager.getCurrentItem());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            tvShows = Parcels.unwrap(savedInstanceState.getParcelable(STATE_TV_SHOWS));
            mSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, -1);
            Timber.tag("on retain").d("tv show size %d, current position %d", tvShows.size(), tvShowViewPager.getCurrentItem());
            progressBar.setVisibility(View.GONE);
        }

        initTvShowViewPager();
    }

    private void initTvShowViewPager() {
        scaleResponsiblePagerAdapter = new ScaleResponsiblePagerAdapter(getActivity(),
                tvShows, getActivity().getSupportFragmentManager());
        int pageMargin = ((Resources.getSystem().getDisplayMetrics().widthPixels / 10) * 2);
        tvShowViewPager.setPageMargin(-pageMargin);
        tvShowViewPager.setAdapter(scaleResponsiblePagerAdapter);
        tvShowViewPager.addOnPageChangeListener(scaleResponsiblePagerAdapter);
        if (mSelectedPosition != -1) {
            tvShowViewPager.setCurrentItem(mSelectedPosition, true);
            if (tvShowViewPager.getVisibility() == View.GONE) {
                tvShowViewPager.setVisibility(View.VISIBLE);
            }
            Timber.tag("init TvShowViewPager").d("current position %d", tvShowViewPager.getCurrentItem());
        }
    }

    @Override
    public void showProgress() {
        tvShowViewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressWithError(String reason) {
        // skip
    }

    @Override
    public void hideProgress() {
        tvShowViewPager.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSimilarShows(List<TvShow> similarTvShows) {
        tvShows.add(selectedTvShow);
        tvShows.addAll(similarTvShows);
        scaleResponsiblePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public long getUserSelectedTvShowId() {
        return selectedTvShow.getId();
    }
}

package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.f2prateek.dart.InjectExtra;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.BasePresenterFragment;
import tushar.letgo.tmdb.dipendency.feature.tvshowdetails.TvShowDetailModule;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/14/17.
 */

public class TvShowDetailFragment extends BasePresenterFragment<TvShowDetailView, TvShowDetailPresenter> implements TvShowDetailView {
    public static final String TAG = TvShowDetailFragment.class.getName();

    @InjectExtra("tvShow")
    TvShow tvShow;

    @Inject
    TvShowDetailPresenter presenter;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.tv_show_view_pager)
    ViewPager tvShowViewPager;

    List<TvShow> tvShows = new ArrayList<>();

    ScaleResponsiblePagerAdapter scaleResponsiblePagerAdapter;

    public static TvShowDetailFragment newInstance(TvShow tvShow) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("tvShow", Parcels.wrap(tvShow));

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
    public void showSimilarShows(List<TvShow> tvShows) {
        this.tvShows.add(tvShow);
        this.tvShows.addAll(tvShows);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scaleResponsiblePagerAdapter = new ScaleResponsiblePagerAdapter(getActivity(), tvShows,
                        getActivity().getSupportFragmentManager());
                int pageMargin = ((Resources.getSystem().getDisplayMetrics().widthPixels / 10) * 2);
                tvShowViewPager.setPageMargin(-pageMargin);
                tvShowViewPager.setAdapter(scaleResponsiblePagerAdapter);
                tvShowViewPager.getAdapter().notifyDataSetChanged();
                tvShowViewPager.addOnPageChangeListener(scaleResponsiblePagerAdapter);
//                tvShowViewPager.setOffscreenPageLimit(tvShows.size());
            }
        });
    }

    @Override
    public long getUserSelectedTvShowId() {
        return tvShow.getId();
    }
}

package tushar.letgo.tmdb.feature.tvshowdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.f2prateek.dart.InjectExtra;

import org.parceler.Parcels;

import javax.inject.Inject;

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
}

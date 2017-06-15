package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.InjectExtra;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import org.parceler.Parcels;

import butterknife.Bind;
import timber.log.Timber;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.base.BaseFragment;
import tushar.letgo.tmdb.model.TvShow;
import tushar.letgo.tmdb.utility.DateConverter;

public class SingleTvShowDetailFragment extends BaseFragment {

    private static final String STATE_TV_SHOW = "tv_Show";
    private static final String POSITION = "position";
    private static final String SCALE = "scale";

    private int screenWidth;
    private int screenHeight;
    private int position;
    private float scale;

    @InjectExtra(STATE_TV_SHOW)
    TvShow tvShow;

    @Bind(R.id.root_container)
    ScaleResponsibleSlideLayout rootContainer;

    @Bind(R.id.layout_content)
    LinearLayout contentLayout;

    @Bind(R.id.img_tv_show_cover)
    ImageView mCoverImage;

    @Bind(R.id.layout_detail_item_highlight)
    LinearLayout detailItemHighLightLayout;

    @Bind(R.id.tv_show_detail_title)
    TextView tvShowDetailTitle;

    @Bind(R.id.tv_show_detail_rating)
    TextView tvShowDetailRating;

    @Bind(R.id.tv_show_detail_air_time)
    TextView tvShowDetailAirTime;

    @Bind(R.id.tv_show_detail_overview)
    TextView tvShowDetailOverview;

    public static Fragment newInstance(int pos, TvShow tvShow, float scale) {
        Bundle arguments = new Bundle();
        arguments.putInt(POSITION, pos);
        arguments.putFloat(SCALE, scale);
        arguments.putParcelable(STATE_TV_SHOW, Parcels.wrap(tvShow));
        SingleTvShowDetailFragment fragment = new SingleTvShowDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.item_tv_show_details;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvShow != null) {
            outState.putParcelable(STATE_TV_SHOW, Parcels.wrap(tvShow));
            outState.putInt(POSITION, position);
            outState.putFloat(SCALE, scale);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        position = this.getArguments().getInt(POSITION);
        scale = this.getArguments().getFloat(SCALE);

        if (savedInstanceState != null) {
            tvShow = Parcels.unwrap(savedInstanceState.getParcelable(STATE_TV_SHOW));
            Timber.tag("on retain").d("single tv show position %d", position);
        }

        setContent();
    }

    private void setContent() {
        Timber.tag("on single tv show").d("single tv show position %d", position);
        int pageMargin = ((Resources.getSystem().getDisplayMetrics().widthPixels / 10) * 2);
        int pageMargin1 = ((Resources.getSystem().getDisplayMetrics().heightPixels / 8) * 2);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(screenWidth - pageMargin, screenHeight - pageMargin1);
        contentLayout.setLayoutParams(layoutParams);

        Glide.with(getActivity()).load(tvShow.getBackdropPath())
                .placeholder(R.color.tv_show_poster_placeholder)
                .centerCrop()
                .crossFade()
                .listener(GlidePalette.with(tvShow.getBackdropPath())
                        .use(GlidePalette.Profile.MUTED_DARK)
                        .intoBackground(detailItemHighLightLayout)
                        .intoTextColor(tvShowDetailTitle, BitmapPalette.Swatch.BODY_TEXT_COLOR)
                        .intoTextColor(tvShowDetailAirTime, BitmapPalette.Swatch.TITLE_TEXT_COLOR))
                .into(mCoverImage);

        tvShowDetailTitle.setText(tvShow.getTitle());
        tvShowDetailRating.setText(String.valueOf(tvShow.getVoteAverage()));
        tvShowDetailAirTime.setText(getString(R.string.dot) + " " +
                DateConverter.getDisplayReleaseTime(tvShow.getFirstAirDate()));
        tvShowDetailOverview.setText(tvShow.getOverview());

        rootContainer.setScale(scale);
    }
}

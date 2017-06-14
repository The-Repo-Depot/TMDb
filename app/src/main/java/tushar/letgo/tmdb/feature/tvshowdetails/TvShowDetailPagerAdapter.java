package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.model.TvShow;

public class TvShowDetailPagerAdapter extends PagerAdapter {

    protected Context context;
    protected List<TvShow> tvShows;
    protected LayoutInflater layoutInflater;


    public TvShowDetailPagerAdapter(Context context, List<TvShow> tvShows) {
        this.context = context;
        this.tvShows = tvShows;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tvShows.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = layoutInflater.inflate(R.layout.item_tv_show_details, container, false);
        ImageView mCoverImage = ButterKnife.findById(view, R.id.show_cover);
        Glide.with(context).load(tvShows.get(position).getBackdropPath())
                .placeholder(R.color.tv_show_poster_placeholder)
                .centerCrop()
                .crossFade()
                .into(mCoverImage);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

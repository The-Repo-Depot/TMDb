package tushar.letgo.tmdb.feature.tvshowlisting;

import android.content.Context;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.base.BaseRecyclerViewAdapter;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/13/17.
 */

public class TvShowRecyclerViewAdapter extends BaseRecyclerViewAdapter<TvShow, RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_PROGRESS = 1;

    private Context mContext;
    private OnTvShowClickListener mOnTvShowClickListener;

    public TvShowRecyclerViewAdapter(Context context, List<TvShow> tvShows, OnTvShowClickListener listener) {
        super(tvShows);

        this.mContext = context;
        this.mOnTvShowClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).equals(ProgressTvShow.INSTANCE) ? VIEW_TYPE_PROGRESS : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);
            viewHolder = new ItemTvShowViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemTvShowViewHolder) {
            final ItemTvShowViewHolder mItemViewHolder = (ItemTvShowViewHolder) holder;
            final TvShow tvShow = getItem(position);

            mItemViewHolder.mTvShow = tvShow;
            mItemViewHolder.mTitleView.setText(tvShow.getTitle());
            mItemViewHolder.mRatingView.setText(tvShow.getTitle());

            if (mItemViewHolder.mTvShowId != tvShow.getId()) {
                resetColors(mItemViewHolder);
                mItemViewHolder.mTvShowId = tvShow.getId();
            }

            Glide.with(mContext)
                    .load(tvShow.getPosterPath())
                    .crossFade()
                    .placeholder(R.color.tv_show_poster_placeholder)
                    .listener(GlidePalette.with(tvShow.getPosterPath())
                            .intoCallBack(palette -> applyColors(mItemViewHolder, palette.getVibrantSwatch())))
                    .into(mItemViewHolder.mImageView);
        }
    }

    private class ItemTvShowViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @Bind(R.id.tv_show_item_container)
        View mContentContainer;

        @Bind(R.id.tv_show_item_image)
        ImageView mImageView;

        @Bind(R.id.tv_show_item_footer)
        View mFooterView;

        @Bind(R.id.tv_show_item_title)
        TextView mTitleView;

        @Bind(R.id.tv_Show_item_rating)
        TextView mRatingView;

        @BindColor(R.color.colorPrimary)
        int mColorBackground;

        @BindColor(R.color.title_text_color)
        int mColorTitle;

        @BindColor(R.color.rating_text_color)
        int mColorRating;

        TvShow mTvShow;

        long mTvShowId;

        public ItemTvShowViewHolder(View view) {
            super(view);
            this.view = view;

            ButterKnife.bind(this, view);
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View view) {
            super(view);
        }
    }

    private void resetColors(ItemTvShowViewHolder mItemViewHolder) {
        mItemViewHolder.mFooterView.setBackgroundColor(mItemViewHolder.mColorBackground);
        mItemViewHolder.mTitleView.setTextColor(mItemViewHolder.mColorTitle);
        mItemViewHolder.mRatingView.setTextColor(mItemViewHolder.mColorRating);
    }

    private void applyColors(ItemTvShowViewHolder mItemViewHolder, Palette.Swatch swatch) {
        if (swatch != null) {
            mItemViewHolder.mFooterView.setBackgroundColor(swatch.getRgb());
            mItemViewHolder.mTitleView.setTextColor(swatch.getBodyTextColor());
            mItemViewHolder.mRatingView.setTextColor(swatch.getTitleTextColor());
        }
    }
}

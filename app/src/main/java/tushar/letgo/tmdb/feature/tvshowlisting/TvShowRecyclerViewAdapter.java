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
        setHasStableIds(true);
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).equals(ProgressTvShow.INSTANCE) ? VIEW_TYPE_PROGRESS : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).equals(ProgressTvShow.INSTANCE) ? -1 : objects.get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
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
            ((ItemTvShowViewHolder) holder).bind(position);
        }
    }

    public class ItemTvShowViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(final int position) {
            mTvShow = objects.get(position);
            mTitleView.setText(mTvShow.getTitle());
            mRatingView.setText(mContext.getString(R.string.tv_rating, mTvShow.getVoteAverage()));

            if (mTvShowId != mTvShow.getId()) {
                resetColors();
                mTvShowId = mTvShow.getId();
            }

            Glide.with(mContext)
                    .load(mTvShow.getPosterPath())
                    .crossFade()
                    .placeholder(R.color.tv_show_poster_placeholder)
                    .listener(GlidePalette.with(mTvShow.getPosterPath())
                            .intoCallBack(palette -> applyColors(palette.getVibrantSwatch())))
                    .into(mImageView);

            mContentContainer.setOnClickListener(v -> {
                if (mOnTvShowClickListener != null) {
                    mOnTvShowClickListener.onTvShowSelected(mContentContainer,
                            mTvShow, position);
                }
            });
        }

        private void resetColors() {
            mFooterView.setBackgroundColor(mColorBackground);
            mTitleView.setTextColor(mColorTitle);
            mRatingView.setTextColor(mColorRating);
        }

        private void applyColors(Palette.Swatch swatch) {
            if (swatch != null) {
                mFooterView.setBackgroundColor(swatch.getRgb());
                mTitleView.setTextColor(swatch.getBodyTextColor());
                mRatingView.setTextColor(swatch.getTitleTextColor());
            }
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View view) {
            super(view);
        }
    }
}

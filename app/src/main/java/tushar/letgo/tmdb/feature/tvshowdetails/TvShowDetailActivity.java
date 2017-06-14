package tushar.letgo.tmdb.feature.tvshowdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.f2prateek.dart.InjectExtra;

import org.parceler.Parcels;

import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.common.mvp.PresenterActivity;
import tushar.letgo.tmdb.model.TvShow;

/**
 * Created by Tushar on 6/14/17.
 */

public class TvShowDetailActivity extends PresenterActivity {

    @InjectExtra("tvShow")
    TvShow tvShow;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void open(Activity activity, View sourceView, TvShow tvShow) {
        Intent startIntent = new Intent(activity, TvShowDetailActivity.class);
        startIntent.putExtra("tvShow", Parcels.wrap(tvShow));

        if (sourceView != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, sourceView, activity.getString(R.string.transition_animation_image));
            activity.startActivity(startIntent, options.toBundle());
        } else {
            activity.startActivity(startIntent);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tv_show_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root, TvShowDetailFragment.newInstance(tvShow), TvShowDetailFragment.TAG)
                    .commit();
        }
    }
}

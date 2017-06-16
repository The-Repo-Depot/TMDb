package tushar.letgo.tmdb.feature.tvshowdetails;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.List;

import tushar.letgo.tmdb.R;
import tushar.letgo.tmdb.model.TvShow;

public class ScaleResponsiblePagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SLIDE = 1f;
    public final static float SMALL_SLIDE = 0.90f;
    public final static float DIFF_SLIDE = BIG_SLIDE - SMALL_SLIDE;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    private List<TvShow> tvShows;

    private float scale;

    public ScaleResponsiblePagerAdapter(Context context, List<TvShow> tvShows, FragmentManager fm) {
        super(fm);
        this.tvShows = tvShows;
    }

    @Override
    public Fragment getItem(int position) {
        scale = SMALL_SLIDE;
        return SingleTvShowDetailFragment.newInstance(position, tvShows.get(position), scale);
    }

    @Override
    public int getCount() {
        return tvShows.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Timber.d("position: %4f", positionOffset);
//        Timber.d("position no: ", position);

        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                ScaleResponsibleSlideLayout cur = (ScaleResponsibleSlideLayout) getRegisteredFragment(position).getView().findViewById(R.id.root_container);
                ScaleResponsibleSlideLayout next = (ScaleResponsibleSlideLayout) getRegisteredFragment(position + 1).getView().findViewById(R.id.root_container);
                cur.setScale(BIG_SLIDE - DIFF_SLIDE * positionOffset);
                next.setScale(SMALL_SLIDE + DIFF_SLIDE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
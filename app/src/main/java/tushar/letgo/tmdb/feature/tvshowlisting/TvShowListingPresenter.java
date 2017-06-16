package tushar.letgo.tmdb.feature.tvshowlisting;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tushar.letgo.tmdb.BuildConfig;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.api.NetworkCallback;
import tushar.letgo.tmdb.common.mvp.BasePresenter;
import tushar.letgo.tmdb.model.Response;

/**
 * Created by Tushar on 6/13/17.
 */

public class TvShowListingPresenter extends BasePresenter<TvShowListingView> {

    private ApiService apiService;

    private int pageNumber = 1;

    private Subscriber subscriber;

    public TvShowListingPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void bindView(TvShowListingView view) {
        super.bindView(view);

        if (!wasViewCreated) {
            if (view != null) {
                view.showProgress();
                loadTvShows();
            }
        }
    }

    private void loadTvShows() {
        apiService.getPopularTvShows(BuildConfig.TMDB_API_KEY, "en-US", pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createSubscriber());
    }

    private Observer<Response> createSubscriber() {
        subscriber = new NetworkCallback<Response>() {
            @Override
            public void onSuccess(Response model) {
                if (view != null) {
                    if (pageNumber == 1) {
                        view.hideErrors();
                        if (view.isRefreshing()) {
                            view.hideRefreshing();
                        } else {
                            view.hideProgress();
                        }
                    } else {
                        view.hideTvShowsLoadingProgress();
                    }
                    view.showTvShows(model.tvShows);
                }
            }

            @Override
            public void onFailure(String message) {
                if (view != null) {
                    if (pageNumber == 1) {
                        if (view.isRefreshing()) {
                            view.hideRefreshing();
                        } else {
                            view.hideProgress();
                            view.showTvShowLoadingError();
                        }
                    } else {
                        view.hideTvShowsLoadingProgress();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        };

        return subscriber;
    }

    public void loadMore() {
        pageNumber++;
        loadTvShows();
        view.showTvShowsLoadingProgress();
    }

    public void reload() {
        pageNumber = 1;
        loadTvShows();
    }

    public void reloadWhenError() {
        pageNumber = 1;
        view.showProgress();
        loadTvShows();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriber != null) {
            subscriber.unsubscribe();
        }
    }
}

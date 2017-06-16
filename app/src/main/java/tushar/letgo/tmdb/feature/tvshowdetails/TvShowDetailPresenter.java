package tushar.letgo.tmdb.feature.tvshowdetails;

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
 * Created by Tushar on 6/14/17.
 */

public class TvShowDetailPresenter extends BasePresenter<TvShowDetailView> {

    private ApiService apiService;

    private int pageNumber = 1;

    private Subscriber subscriber;

    public TvShowDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void bindView(TvShowDetailView view) {
        super.bindView(view);
        if (!wasViewCreated) {
            if (view != null) {
                view.showProgress();
                loadSimilarTvShows();
            }
        }
    }

    public void loadSimilarShowsWhenError() {
        view.showProgress();
        loadSimilarTvShows();
    }

    private void loadSimilarTvShows() {
        apiService.getSimilarTvShows(
                String.valueOf(view.getUserSelectedTvShowId()),
                BuildConfig.TMDB_API_KEY,
                "en-US",
                pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriber());
    }

    private Observer<Response> getSubscriber() {
        subscriber = new NetworkCallback<Response>() {
            @Override
            public void onSuccess(Response model) {
                if (view != null) {
                    view.hideProgress();
                    view.showSimilarShows(model.tvShows);
                }
            }

            @Override
            public void onFailure(String message) {
                if (view != null) {
                    view.hideProgressWithError(message);
                }
            }

            @Override
            public void onFinish() {

            }
        };
        return subscriber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriber != null) {
            subscriber.unsubscribe();
        }
    }
}

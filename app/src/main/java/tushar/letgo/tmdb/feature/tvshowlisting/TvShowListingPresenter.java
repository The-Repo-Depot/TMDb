package tushar.letgo.tmdb.feature.tvshowlisting;

import retrofit2.Call;
import retrofit2.Callback;
import tushar.letgo.tmdb.BuildConfig;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.common.mvp.BasePresenter;
import tushar.letgo.tmdb.model.Response;

/**
 * Created by Tushar on 6/13/17.
 */

public class TvShowListingPresenter extends BasePresenter<TvShowListingView> {

    private ApiService apiService;

    private int pageNumber = 1;

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
                .enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (view != null) {
                            if (pageNumber == 1) {
                                if (view.isRefreshing()) {
                                    view.hideRefreshing();
                                } else {
                                    view.hideProgress();
                                }
                            } else {
                                view.hideTvShowsLoadingProgress();
                            }
                            view.showTvShows(response.body().tvShows);
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
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
                });
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
}

package tushar.letgo.tmdb.feature.tvshowdetails;

import retrofit2.Call;
import retrofit2.Callback;
import tushar.letgo.tmdb.BuildConfig;
import tushar.letgo.tmdb.api.ApiService;
import tushar.letgo.tmdb.common.mvp.BasePresenter;
import tushar.letgo.tmdb.model.Response;

/**
 * Created by Tushar on 6/14/17.
 */

public class TvShowDetailPresenter extends BasePresenter<TvShowDetailView> {

    private ApiService apiService;

    private int pageNumber = 1;

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

    private void loadSimilarTvShows() {
        apiService.getSimilarTvShows(
                String.valueOf(view.getUserSelectedTvShowId()),
                BuildConfig.TMDB_API_KEY,
                "en-US",
                pageNumber
        ).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (view != null) {
                    view.hideProgress();
                    view.showSimilarShows(response.body().tvShows);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                if (view != null) {
                    view.hideProgress();
                }
            }
        });
    }
}

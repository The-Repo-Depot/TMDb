package tushar.letgo.tmdb.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tushar.letgo.tmdb.BuildConfig;
import tushar.letgo.tmdb.model.Response;

/**
 * Created by Tushar on 6/13/17.
 */

public interface ApiService {
    String URL = BuildConfig.TMDB_API_URL;

    @GET("popular")
    Observable<Response> getPopularTvShows(@Query("api_key") String apiKey,
                                           @Query("language") String language,
                                           @Query("page") int pageNumber);

    @GET("{id}/similar")
    Observable<Response> getSimilarTvShows(@Path("id") String tvShowId,
                                           @Query("api_key") String apiKey,
                                           @Query("language") String language,
                                           @Query("page") int pageNumber);
}

package tushar.letgo.tmdb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Tushar on 6/13/17.
 */

@Parcel
public class TvShow {

    @Expose
    long id;

    @Expose
    @SerializedName("original_name")
    String title;

    @Expose
    String overview;

    @Expose
    @SerializedName("poster_path")
    String posterPath;

    @Expose
    @SerializedName("backdrop_path")
    String backdropPath;

    @Expose
    @SerializedName("vote_average")
    double voteAverage;

    @Expose
    @SerializedName("first_air_date")
    String firstAirDate;

    public TvShow() {
    }

    public long getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }
}

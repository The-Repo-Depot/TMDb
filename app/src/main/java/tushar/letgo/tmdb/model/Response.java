package tushar.letgo.tmdb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public final class Response {

    @Expose
    public int page;

    @Expose
    @SerializedName("total_pages")
    public int totalPages;

    @Expose
    @SerializedName("total_results")
    public int totalShows;

    @Expose
    @SerializedName("results")
    public List<TvShow> tvShows = new ArrayList<>();
}
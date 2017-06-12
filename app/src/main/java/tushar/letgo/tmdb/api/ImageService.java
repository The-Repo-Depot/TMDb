package tushar.letgo.tmdb.api;

import tushar.letgo.tmdb.BuildConfig;

public final class ImageService {

    public static final String BASE_URL = BuildConfig.TMDB_IMAGE_URL;

    public static String buildPosterUrl(String imagePath, int width) {
        String widthPath;

        if (width <= 92) {
            widthPath = "/w92";
        } else if (width <= 154) {
            widthPath = "/w154";
        } else if (width <= 185) {
            widthPath = "/w185";
        } else if (width <= 342) {
            widthPath = "/w342";
        } else if (width <= 500) {
            widthPath = "/w500";
        } else {
            widthPath = "/w780";
        }

        return BASE_URL + widthPath + imagePath;
    }

}
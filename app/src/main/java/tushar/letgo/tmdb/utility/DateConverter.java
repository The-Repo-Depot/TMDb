package tushar.letgo.tmdb.utility;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Tushar on 6/15/17.
 */

public final class DateConverter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static String getDisplayReleaseTime(String releaseDate) {
        if (TextUtils.isEmpty(releaseDate)) {
            return "";
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DATE_FORMAT.parse(releaseDate));
            return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            Timber.e(e, "Failed to parse release date.");
            return "";
        }
    }
}

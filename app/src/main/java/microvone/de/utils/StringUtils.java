package microvone.de.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jörn on 09.09.2017.
 */

public class StringUtils {

    /**
     *
     */
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Formatiere ein Datumswert in ein Stringausgabe mit Format: yyyy-MM-dd HH:mm:ss
     * @param now  Aktuelles Datum
     * @return  Stringausgabe
     */
    public static String formateDateToString(Date now) {
        if(now == null) {
            throw new IllegalArgumentException("Date cannot be NULL");
        }

        return SDF.format(now);
    }

}

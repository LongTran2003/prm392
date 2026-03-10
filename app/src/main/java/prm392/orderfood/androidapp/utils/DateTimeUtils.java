package prm392.orderfood.androidapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);

        // Chỉ lấy giờ và phút
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        return dateFormat.format(date);
    }
}

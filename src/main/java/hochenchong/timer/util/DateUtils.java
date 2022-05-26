package hochenchong.timer.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 日期工具类
 *
 * @author hochenchong
 */
public class DateUtils {

    /**
     * 判断两个日期是否在同一周
     *
     * @param localDate1 日期1
     * @param localDate2 日期2
     * @return true：同一周，false：不是同一周
     */
    public static boolean isSameWeek(LocalDate localDate1, LocalDate localDate2) {
        if (localDate1 == null || localDate2 == null) {
            return false;
        }
        LocalDate monday1 = localDate1.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate monday2 = localDate2.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return monday1.compareTo(monday2) == 0;
    }
}

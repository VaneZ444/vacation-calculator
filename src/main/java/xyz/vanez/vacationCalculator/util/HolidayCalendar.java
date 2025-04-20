package xyz.vanez.vacationCalculator.util;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Component
public class HolidayCalendar {
    private final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2025, 1, 1), // Новый год
            LocalDate.of(2025, 1, 7) // Рождество
    );

    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date) ||
                date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
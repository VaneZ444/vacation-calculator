package xyz.vanez.vacationCalculator.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.vanez.vacationCalculator.dto.VacationPayRequest;
import xyz.vanez.vacationCalculator.dto.VacationPayResponse;
import xyz.vanez.vacationCalculator.util.HolidayCalendar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacationPayServiceImplTest {

    @Mock
    private HolidayCalendar holidayCalendar;

    @InjectMocks
    private VacationPayServiceImpl vacationPayService;

    @Test
    void calculate_withVacationDays_shouldReturnCorrectAmount() {
        VacationPayRequest request = VacationPayRequest.createWithDays(150000.0, 14);
        VacationPayResponse response = vacationPayService.calculate(request);

        BigDecimal expected = BigDecimal.valueOf(150000.0)
                .divide(BigDecimal.valueOf(29.3), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(14))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, response.getAmount());
    }

    @Test
    void calculate_withDateRangeIncludingHolidays_shouldExcludeNonWorkingDays() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 7);

        // Настройка моков для всех дней в диапазоне
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 1))).thenReturn(true); // Праздник
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 2))).thenReturn(false); // Рабочий
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 3))).thenReturn(false); // Рабочий
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 4))).thenReturn(true); // Суббота
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 5))).thenReturn(true); // Воскресенье
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 6))).thenReturn(false); // Рабочий
        when(holidayCalendar.isHoliday(LocalDate.of(2025, 1, 7))).thenReturn(true); // Праздник

        VacationPayRequest request = VacationPayRequest.createWithRange(150000.0, startDate, endDate);
        VacationPayResponse response = vacationPayService.calculate(request);

        BigDecimal expected = BigDecimal.valueOf(150000.0)
                .divide(BigDecimal.valueOf(29.3), 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(3))
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, response.getAmount());
    }

    private BigDecimal calculateExpectedAmount(double averageSalary, long days) {
        return BigDecimal.valueOf(averageSalary)
                .divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP)
                .divide(BigDecimal.valueOf(29.3), 10, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(days))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}

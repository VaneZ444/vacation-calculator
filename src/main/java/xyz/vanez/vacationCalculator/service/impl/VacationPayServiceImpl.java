package xyz.vanez.vacationCalculator.service.impl;

import xyz.vanez.vacationCalculator.dto.VacationPayRequest;
import xyz.vanez.vacationCalculator.dto.VacationPayResponse;
import xyz.vanez.vacationCalculator.exception.InvalidDateRangeException;
import xyz.vanez.vacationCalculator.service.VacationPayService;
import xyz.vanez.vacationCalculator.util.HolidayCalendar;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class VacationPayServiceImpl implements VacationPayService {
    private static final BigDecimal AVERAGE_WORKING_DAYS_PER_MONTH = BigDecimal.valueOf(29.3);
    private final HolidayCalendar holidayCalendar;

    public VacationPayServiceImpl(HolidayCalendar holidayCalendar) {
        this.holidayCalendar = holidayCalendar;
    }

    @Override
    public VacationPayResponse calculate(VacationPayRequest request) {
        validateRequest(request);

        BigDecimal amount = calculateDailySalary(request.getAverageSalary())
                .multiply(BigDecimal.valueOf(calculateWorkingDays(request)))
                .setScale(2, RoundingMode.HALF_UP);

        return new VacationPayResponse(amount);
    }

    private void validateRequest(VacationPayRequest request) {
        request.validate();
    }

    private int calculateWorkingDays(VacationPayRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null) {
            return calculateExactWorkingDays(request.getStartDate(), request.getEndDate());
        } else {
            return request.getVacationDays();
        }
    }

    private int calculateExactWorkingDays(LocalDate start, LocalDate end) {
        int workingDays = 0;
        LocalDate date = start;

        while (!date.isAfter(end)) {
            if (!holidayCalendar.isHoliday(date)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }

        return workingDays;
    }

    private BigDecimal calculateDailySalary(Double averageSalary) {
        return BigDecimal.valueOf(averageSalary)
                .divide(AVERAGE_WORKING_DAYS_PER_MONTH, 10, RoundingMode.HALF_UP);
    }
}
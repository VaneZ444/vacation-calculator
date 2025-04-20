package xyz.vanez.vacationCalculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "Параметры для расчета отпускных")
public class VacationPayRequest {
    @Positive(message = "Зарплата должна быть положительной")
    @NotNull(message = "Зарплата обязательна")
    @Schema(description = "Средняя зарплата за 12 месяцев", example = "150000")
    private Double averageSalary;

    @Min(value = 1, message = "Минимум 1 день отпуска")
    @Schema(description = "Количество дней отпуска", example = "14")
    private Integer vacationDays;

    @FutureOrPresent(message = "Дата начала должна быть сегодня или позже")
    @Schema(description = "Дата начала отпуска", example = "2025-07-01")
    private LocalDate startDate;

    @FutureOrPresent(message = "Дата конца должна быть сегодня или позже")
    @Schema(description = "Дата окончания отпуска", example = "2025-07-14")
    private LocalDate endDate;

    public static VacationPayRequest createWithDays(Double averageSalary, Integer vacationDays) {
        return VacationPayRequest.builder().averageSalary(averageSalary).vacationDays(vacationDays).build();
    }

    public static VacationPayRequest createWithRange(Double averageSalary, LocalDate startDate, LocalDate endDate) {
        return VacationPayRequest.builder().averageSalary(averageSalary).startDate(startDate).endDate(endDate).build();
    }

    public void validate() {
        if (averageSalary == null) {
            throw new IllegalArgumentException("Средняя зарплата обязательна");
        }

        if (vacationDays != null && (startDate != null || endDate != null)) {
            throw new IllegalArgumentException("Укажите либо количество дней, либо диапазон дат");
        }

        if (vacationDays == null && (startDate == null || endDate == null)) {
            throw new IllegalArgumentException("Укажите количество дней отпуска ИЛИ диапазон дат");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Дата начала отпуска не может быть позже даты окончания");
        }
    }
}
package xyz.vanez.vacationCalculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Результат расчета отпускных")
public class VacationPayResponse {
    @Schema(description = "Сумма отпускных", example = "14334.46")
    private BigDecimal amount;
}
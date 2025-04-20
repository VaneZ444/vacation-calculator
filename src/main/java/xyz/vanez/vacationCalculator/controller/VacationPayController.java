package xyz.vanez.vacationCalculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.vanez.vacationCalculator.dto.ApiError;
import xyz.vanez.vacationCalculator.dto.VacationPayRequest;
import xyz.vanez.vacationCalculator.dto.VacationPayResponse;
import xyz.vanez.vacationCalculator.service.VacationPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/api/v1/vacation")
@RequiredArgsConstructor
public class VacationPayController {
    private final VacationPayService vacationPayService;

    @Operation(
            summary = "Рассчитать отпускные",
            description = "Возвращает сумму отпускных. Можно указать либо количество дней, либо диапазон дат",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный расчет",
                            content = @Content(schema = @Schema(implementation = VacationPayResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидные параметры",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Внутренняя ошибка сервера",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\n" +
                                                    "  \"timestamp\": \"2025-04-20T18:57:34.926Z\",\n" +
                                                    "  \"status\": 500,\n" +
                                                    "  \"error\": \"Internal Server Error\",\n" +
                                                    "  \"message\": \"Internal server error\"\n" +
                                                    "}"
                                    )
                            )
                    )}
    )
    @GetMapping("/calculate")
    public ResponseEntity<?> calculateVacationPay(
            @Parameter(description = "Средняя зарплата за 12 месяцев", required = true)
            @RequestParam @Valid Double averageSalary,

            @Parameter(description = "Количество дней отпуска (нельзя использовать одновременно с диапазоном дат)")
            @RequestParam(required = false) @Valid Integer vacationDays,

            @Parameter(description = "Дата начала отпуска (формат: YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Дата окончания отпуска (формат: YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

    VacationPayRequest request;

        if (startDate != null && endDate != null && vacationDays == null) {
            request = VacationPayRequest.createWithRange(averageSalary, startDate, endDate);
        } else if (vacationDays != null && startDate == null && endDate == null ) {
            request = VacationPayRequest.createWithDays(averageSalary, vacationDays);
        } else {
            throw new IllegalArgumentException("Необходимо указать либо количество дней отпуска, либо диапазон дат");
        }

        request.validate();
        return ResponseEntity.ok(vacationPayService.calculate(request));
    }
}
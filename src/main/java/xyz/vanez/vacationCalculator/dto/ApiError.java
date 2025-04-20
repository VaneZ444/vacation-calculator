package xyz.vanez.vacationCalculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@Data
@Schema(description = "Описание ошибки API")
public class ApiError {
    @Schema(description = "Время возникновения ошибки", example = "2025-02-20T14:30:00")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "HTTP статус код", example = "400")
    private int status;

    @Schema(description = "Тип ошибки", example = "Bad Request")
    private String error;

    @Schema(description = "Сообщение об ошибке", example = "Невалидные параметры")
    private String message;

    public ApiError(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
}
package xyz.vanez.vacationCalculator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vanez.vacationCalculator.dto.VacationPayRequest;
import xyz.vanez.vacationCalculator.dto.VacationPayResponse;

@Configuration
public class AppConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vacation Pay Calculator API")
                        .version("1.0")
                        .description("Микросервис для расчета отпускных")
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                        .components(new Components()
                            .addSchemas("VacationPayRequest", new Schema<VacationPayRequest>())
                            .addSchemas("VacationPayResponse", new Schema<VacationPayResponse>()));
    }
}

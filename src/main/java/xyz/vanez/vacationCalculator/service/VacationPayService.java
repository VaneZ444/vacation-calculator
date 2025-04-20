package xyz.vanez.vacationCalculator.service;

import xyz.vanez.vacationCalculator.dto.VacationPayRequest;
import xyz.vanez.vacationCalculator.dto.VacationPayResponse;

public interface VacationPayService {
    VacationPayResponse calculate(VacationPayRequest request);
}

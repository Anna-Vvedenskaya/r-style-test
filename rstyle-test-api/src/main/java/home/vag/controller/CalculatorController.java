package home.vag.controller;

import home.vag.business.dto.AnnuityScheduleParametersDTO;
import home.vag.business.dto.CreditRepaymentDTO;
import home.vag.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/calculator")
public class CalculatorController {

    /**
     * Годовая процентная ставка в %.
     * Задается константой, так как сказано, что пользователь не может ее менять.
     * В реальном приложении должна определяться как-то по-другому.
     */
    private static final double RATE = 14.2;

    @Autowired
    private CalculatorService calculatorService;

    @GetMapping(path = "/rate")
    public double getRate() {
        return RATE;
    }

    @PostMapping(path = "/annuity_schedule")
    public List<CreditRepaymentDTO> calculateAnnuitySchedule(@RequestBody AnnuityScheduleParametersDTO dto) {
        return calculatorService.calculateAnnuitySchedule(dto);
    }
}

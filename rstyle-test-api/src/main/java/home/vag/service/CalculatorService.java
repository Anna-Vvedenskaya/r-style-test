package home.vag.service;

import home.vag.business.dto.AnnuityScheduleParametersDTO;
import home.vag.business.dto.CreditRepaymentDTO;

import java.util.List;

/**
 * Сервис кредитного калькулятора
 */
public interface CalculatorService {
    /**
     * Расчет графика погашения.
     * Данные отсортированы по году и месяцу.
     * @param dto параметры кредита
     * @return график погашения
     */
    List<CreditRepaymentDTO> calculateAnnuitySchedule(AnnuityScheduleParametersDTO dto);
}

package home.vag.business.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Модель входных параметров для расчета графика погашения кредита
 */
@Builder
@Getter
public class AnnuityScheduleParametersDTO {
    /**
     * Сумма кредита (в копейках)
     */
    private long sum;
    /**
     * Срок кредита (в месяцах)
     */
    private int monthCount;
    /**
     * Годовая процентная ставка в %
     */
    private double rate;
}

package home.vag.business.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Модель данных графика погашения кредита за определенный месяц
 */
@Builder
@Getter
public class CreditRepaymentDTO {
    /**
     * Год
     */
    private int year;
    /**
     * Месяц, нумерация с 0
     */
    private int month;
    /**
     * Общая сумма платежа
     */
    private long payment;
    /**
     * Платеж по процентам
     */
    private long percentPayment;
    /**
     * Платеж по основному долгу
     */
    private long debtPayment;
    /**
     * Остаток основного долга
     */
    private long debt;

    /**
     * @return остаток основного долга на начало следующего месяца
     */
    public long calculateNextDebt() {
        return debt - debtPayment;
    }
}

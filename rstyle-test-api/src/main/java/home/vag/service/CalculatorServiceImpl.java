package home.vag.service;

import home.vag.business.dto.AnnuityScheduleParametersDTO;
import home.vag.business.dto.CreditRepaymentDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    /**
     * Минимальный допустимый срок кредита (в месяцах)
     */
    public static final int MONTH_COUNT_MIN = 12;
    /**
     * Максимальный допустимый срок кредита (в месяцах)
     */
    public static final int MONTH_COUNT_MAX = 60;
    /**
     * Минимальная допустимая сумма кредита (в копейках)
     */
    public static final long SUM_MIN = 100_000_00;
    /**
     * Максимальная допустимая сумма кредита (в копейках)
     */
    public static final long SUM_MAX = 5_000_000_00;

    @Override
    public List<CreditRepaymentDTO> calculateAnnuitySchedule(AnnuityScheduleParametersDTO dto) {
        final int monthCount = dto.getMonthCount();
        if (monthCount < MONTH_COUNT_MIN || monthCount > MONTH_COUNT_MAX) {
            throw new RuntimeException("Допустимые значения срока кредита - от 12 до 60.");
        }
        if (dto.getSum() < SUM_MIN || dto.getSum() > SUM_MAX) {
            throw new RuntimeException("Допустимые значения суммы кредита - от 100 000 до 5 000 000.");
        }

        final List<CreditRepaymentDTO> result = new ArrayList<>();
        final LocalDate startDate = LocalDate.now().plusMonths(1);
        int year = startDate.getYear();
        Month month = startDate.getMonth();
        final double rate = dto.getRate() / 100.;
        final double ratePerMonth = rate / 12.;
        final long monthPayment = Math.round(dto.getSum() * ratePerMonth
                / (1. - Math.pow(1. + ratePerMonth, -monthCount)));
        CreditRepaymentDTO previousDTO = null;

        for (int i = 0; i < monthCount; ++i) {
            final long debt = isNull(previousDTO) ? dto.getSum() : previousDTO.calculateNextDebt();
            // Для соблюдения условия равенства платежей накопленную погрешность
            // компенсируем в процентной составляющей последнего платежа
            final long percentPayment = i == monthCount - 1
                    ? monthPayment - debt
                    : Math.round(debt * ratePerMonth);
            final long debtPayment = monthPayment - percentPayment;
            final CreditRepaymentDTO nextDTO = CreditRepaymentDTO.builder()
                    .year(year)
                    .month(month.ordinal())
                    .debt(debt)
                    .payment(monthPayment)
                    .percentPayment(percentPayment)
                    .debtPayment(debtPayment)
                    .build();
            result.add(nextDTO);
            previousDTO = nextDTO;
            month = month.plus(1);
            if (month.equals(Month.JANUARY)) {
                ++year;
            }
        }
        return result.stream()
                .sorted(Comparator.comparingInt(CreditRepaymentDTO::getYear)
                        .thenComparing(CreditRepaymentDTO::getMonth))
                .collect(Collectors.toList());
    }
}

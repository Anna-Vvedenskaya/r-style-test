package home.vag.service;

import home.vag.business.dto.AnnuityScheduleParametersDTO;
import home.vag.business.dto.CreditRepaymentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Objects.isNull;
import static org.junit.Assert.*;

import static home.vag.service.CalculatorServiceImpl.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorServiceImplTest {

    @Autowired
    private CalculatorService calculatorService;

    @Test(expected = RuntimeException.class)
    public void validateSumAnnuitySchedule() {
        final AnnuityScheduleParametersDTO parametersDTO = AnnuityScheduleParametersDTO.builder()
                .sum(Math.min(SUM_MIN - 1, Math.round(Math.random() * SUM_MIN)))
                .monthCount(MONTH_COUNT_MIN)
                .rate(12.5)
                .build();
        calculatorService.calculateAnnuitySchedule(parametersDTO);
    }

    @Test(expected = RuntimeException.class)
    public void validateMonthCountAnnuitySchedule() {
        final AnnuityScheduleParametersDTO parametersDTO = AnnuityScheduleParametersDTO.builder()
                .sum(SUM_MIN)
                .monthCount((int)Math.min(MONTH_COUNT_MIN - 1, Math.round(Math.random() * MONTH_COUNT_MIN)))
                .rate(12.5)
                .build();
        calculatorService.calculateAnnuitySchedule(parametersDTO);
    }

    @Test
    public void checkSizeAnnuitySchedule() {
        final AnnuityScheduleParametersDTO parametersDTO = generateCorrectDTO();
        final List<CreditRepaymentDTO> result = calculatorService.calculateAnnuitySchedule(parametersDTO);
        assertEquals(result.size(), parametersDTO.getMonthCount());
    }

    @Test
    public void checkMonthPaymentAnnuitySchedule() {
        final List<CreditRepaymentDTO> result = getResult();
        final long payment = result.get(0).getPayment();
        for (CreditRepaymentDTO dto : result) {
            assertEquals(dto.getPayment(), payment);
        }
    }

    @Test
    public void checkPaymentSumsAnnuitySchedule() {
        final List<CreditRepaymentDTO> result = getResult();
        for (CreditRepaymentDTO dto : result) {
            assertEquals(dto.getPayment(), dto.getDebtPayment() + dto.getPercentPayment());
        }
    }

    @Test
    public void checkDebtAnnuitySchedule() {
        final AnnuityScheduleParametersDTO parametersDTO = generateCorrectDTO();
        final List<CreditRepaymentDTO> result = getResult(parametersDTO);
        CreditRepaymentDTO previousDTO = null;
        for (CreditRepaymentDTO dto : result) {
            if (isNull(previousDTO)) {
                assertEquals(dto.getDebt(), parametersDTO.getSum());
            } else {
                assertEquals(dto.getDebt(), previousDTO.getDebt() - previousDTO.getPayment() + previousDTO.getPercentPayment());
            }
            previousDTO = dto;
        }
        final CreditRepaymentDTO lastDTO = result.get(result.size() - 1);
        assertEquals(lastDTO.getDebt(), lastDTO.getDebtPayment());
    }

    private AnnuityScheduleParametersDTO generateCorrectDTO() {
        return AnnuityScheduleParametersDTO.builder()
                .sum(SUM_MIN  + Math.round(Math.random() * (SUM_MAX - SUM_MIN)))
                .monthCount(MONTH_COUNT_MIN  + (int)Math.round(Math.random() * (MONTH_COUNT_MAX - MONTH_COUNT_MIN)))
                .rate(14.5)
                .build();
    }

    private List<CreditRepaymentDTO> getResult() {
        return getResult(generateCorrectDTO());
    }

    private List<CreditRepaymentDTO> getResult(AnnuityScheduleParametersDTO parametersDTO) {
        final List<CreditRepaymentDTO> result = calculatorService.calculateAnnuitySchedule(parametersDTO);
        assertTrue(result.size() > 0);
        return result;
    }
}
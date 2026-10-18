package ru.vozzhaeva.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.Positions;

import java.time.LocalDate;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService{

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int currentYear = LocalDate.now().getYear();
        int daysInYear = isLeapYear(currentYear) ? 366 : 365;

        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    @Override
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException("Квартальная премия рассчитывается только для менеджеров (isManager = true).");
        }
        return salary * bonus * 90 * positions.getPositionCoefficient() / workDays;
    }
}

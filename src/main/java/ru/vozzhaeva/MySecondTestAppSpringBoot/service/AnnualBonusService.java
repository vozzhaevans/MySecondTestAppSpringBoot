package ru.vozzhaeva.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface AnnualBonusService {
    double calculate(Positions positions, double salary, double bonus, int workDays);
    double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays);
}

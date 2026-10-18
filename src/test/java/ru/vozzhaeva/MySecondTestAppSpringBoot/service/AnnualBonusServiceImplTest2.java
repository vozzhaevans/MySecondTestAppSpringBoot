package ru.vozzhaeva.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.Positions;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnnualBonusServiceImplTest2 {

    @Test
    void calculateQuarterlyBonus() {
        Positions position = Positions.TL;
        double salary = 100000.00;
        double bonus = 2.0;
        int workDays = 60;

        double result = new AnnualBonusServiceImpl().calculateQuarterlyBonus(position, salary, bonus, workDays);
// 100000 * 2.0 * 90 * 2.6 / 60 = 780000.0
        double expected = 780000.0;
        assertThat(result).isEqualTo(expected);

    }
}
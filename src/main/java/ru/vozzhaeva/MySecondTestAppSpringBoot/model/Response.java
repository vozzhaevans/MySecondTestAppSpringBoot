package ru.vozzhaeva.MySecondTestAppSpringBoot.model;


import lombok.Builder;
import lombok.Data;

/**
 * Исходящее сообщение (Response).
 * Содержит результат обработки запроса.
 */
@Data
@Builder
public class Response {

    /**
     * Уникальный идентификатор сообщения.
     * Обязательное.
     */
    private String uid;

    /**
     * Уникальный идентификатор операции.
     * Обязательное.
     */
    private String operationUid;

    /**
     * Время создания сообщения.
     * Обязательное.
     */
    private String systemTime;

    /**
     * Статус обработки запроса.
     * Возможные значения: success, failed.
     */
    private Codes code;

    /**
     * Код ошибки.
     * Возможные значения: UnsupportedCodeException, ValidationException, UnknownException.
     */
    private ErrorCodes errorCode;

    /**
     * Сообщение об ошибке.
     * Возможные значения: "Не поддерживаемая ошибка", "Ошибка валидации", "Произошла непредвиденная ошибка".
     */
    private ErrorMessages errorMessage;

    /**
     * Должность сотрудника (возвращается из запроса).
     */
    private Positions position;

    /**
     * Заработная плата сотрудника.
     */
    private Double salary;

    /**
     * Результат расчета годовой премии.
     */
    private Double bonus;

    /**
     * Результат расчета квартальной премии.
     */
    private Double quarterlyBonus;

    /**
     * Количество отработанных дней.
     */
    private Integer workDays;
}

package ru.vozzhaeva.MySecondTestAppSpringBoot.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Входящее сообщение (Request).
 * Содержит данные, необходимые для обработки запроса и расчета премии.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Уникальный идентификатор сообщения.
     * Обязательное, до 32 символов.
     */
    @NotBlank(message = "uid не может быть пустым")
    @Size(max = 32, message = "uid не должен превышать 32 символа")
    private String uid;

    /**
     * Уникальный идентификатор операции.
     * Обязательное, до 32 символов.
     */
    @NotBlank(message = "operationUid не может быть пустым")
    @Size(max = 32, message = "operationUid не должен превышать 32 символа")
    private String operationUid;

    /**
     * Имя системы отправителя.
     * Необязательное.
     */
    private Systems systemName;

    /**
     * Время создания сообщения.
     * Обязательное.
     */
    @NotBlank(message = "systemTime не может быть пустым")
    private String systemTime;

    /**
     * Наименование ресурса.
     * Необязательное.
     */
    private String source;

    /**
     * Должность сотрудника для расчета премии.
     */
    private Positions position;

    /**
     * Заработная плата сотрудника.
     */
    private Double salary;

    /**
     * Коэффициент бонуса (исходное значение из запроса).
     */
    private Double bonus;

    /**
     * Количество отработанных дней.
     */
    private Integer workDays;

    /**
     * Уникальный идентификатор коммуникации.
     * Обязательное, минимум 1, максимум 100000.
     */
    @NotNull(message = "communicationId не может быть пустым")
    @Min(value = 1, message = "communicationId должен быть не менее 1")
    @Max(value = 100000, message = "communicationId должен быть не более 100000")
    private Integer communicationId;

    /**
     * Уникальный идентификатор шаблона.
     * Необязательное.
     */
    private Integer templateId;

    /**
     * Код продукта.
     * Необязательное.
     */
    private Integer productCode;

    /**
     * Смс код.
     * Необязательное.
     */
    private Integer smsCode;

    /**
     * Время начала обработки запроса (для замера времени).
     * Заполняется автоматически в контроллере.
     */
    private long startTime;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position='" + position + '\'' +
                ", salary='" + salary + '\'' +
                ", bonus='" + bonus + '\'' +
                ", workDays='" + workDays + '\'' +
                ", communicationId='" + communicationId +
                ", templateId='" + templateId +
                ", productCode='" + productCode +
                ", smsCode='" + smsCode +
                ", startTime='" + startTime +
                '}';
    }
}

package ru.vozzhaeva.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vozzhaeva.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.vozzhaeva.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.*;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.AnnualBonusService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ValidationService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySourceRequestService;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("modifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("modifySourceRequestService") ModifyRequestService modifySourceRequestService,
                        AnnualBonusService annualBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        long startTime = System.currentTimeMillis();
        request.setStartTime(startTime);
        log.info("request: {}", request);

        logBindingResultErrors(bindingResult);

        Response response = createBaseResponse(request);

        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("Исключение ValidationFailedException: {}", e.getMessage());
            return buildErrorResponse(response, Codes.FAILED, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Неизвестное исключение: {}", e.getMessage(), e);
            return buildErrorResponse(response, Codes.FAILED, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            calculateBonuses(request, response);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка валидации данных для премии: {}", e.getMessage());
            return buildErrorResponse(response, Codes.FAILED, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Ошибка при расчете премии: {}", e.getMessage(), e);
            return buildErrorResponse(response, Codes.FAILED, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        modifySourceRequestService.modify(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void logBindingResultErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Обнаружены ошибки валидации в BindingResult:");
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.error("Поле: '{}', отклоненное значение: '{}', сообщение: '{}'",
                        error.getField(), error.getRejectedValue(), error.getDefaultMessage());
            }
        }
    }

    private Response createBaseResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private void calculateBonuses(Request request, Response response) {
        Positions position = request.getPosition();
        Double salary = request.getSalary();
        Double bonus = request.getBonus();
        Integer workDays = request.getWorkDays();

        if (position == null || salary == null || bonus == null || workDays == null) {
            throw new IllegalArgumentException("Не все данные для расчета премии переданы в запросе");
        }

        double calculatedAnnualBonus = annualBonusService.calculate(position, salary, bonus, workDays);
        response.setBonus(calculatedAnnualBonus);

        try {
            double calculatedQuarterlyBonus = annualBonusService.calculateQuarterlyBonus(position, salary, bonus, workDays);
            response.setQuarterlyBonus(calculatedQuarterlyBonus);
        } catch (IllegalArgumentException e) {
            log.warn("Квартальная премия не рассчитана: {}", e.getMessage());
            response.setQuarterlyBonus(0.0);
        }

        response.setPosition(position);
        response.setSalary(salary);
        response.setWorkDays(workDays);
    }

    private ResponseEntity<Response> buildErrorResponse(Response response, Codes code,
                                                        ErrorCodes errorCode, ErrorMessages errorMessage,
                                                        HttpStatus status) {
        response.setCode(code);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
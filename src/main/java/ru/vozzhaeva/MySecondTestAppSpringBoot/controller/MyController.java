package ru.vozzhaeva.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError; // ВАЖНО: добавить импорт
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vozzhaeva.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.vozzhaeva.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.*;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.service.ValidationService;
import ru.vozzhaeva.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyRequestService modifySourceRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("modifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                        @Qualifier("modifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService,
                        @Qualifier("modifySourceRequestService") ModifyRequestService modifySourceRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifySourceRequestService = modifySourceRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        long startTime = System.currentTimeMillis();
        request.setStartTime(startTime);
        log.info("request: {}", request);

        if (bindingResult.hasErrors()) {
            log.error("Обнаружены ошибки валидации в BindingResult:");
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.error("Поле: '{}', отклоненное значение: '{}', сообщение: '{}'",
                        error.getField(), error.getRejectedValue(), error.getDefaultMessage());
            }
        }


        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {

            log.error("Исключение ValidationFailedException: {}", e.getMessage(), e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            log.error("Неизвестное исключение: {}", e.getMessage(), e);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        modifySourceRequestService.modify(request);
        modifySystemNameRequestService.modify(request);

        return new ResponseEntity<>(modifyResponseService.modify(response), HttpStatus.OK);
    }
}
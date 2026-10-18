package ru.vozzhaeva.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}

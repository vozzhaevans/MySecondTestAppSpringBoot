package ru.vozzhaeva.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.vozzhaeva.MySecondTestAppSpringBoot.model.Request;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}

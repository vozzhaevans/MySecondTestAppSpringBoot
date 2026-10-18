package ru.vozzhaeva.MySecondTestAppSpringBoot.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Systems {

    ERP("Enterprise Resource Planning"),
    CRM("Customer Relationship Management"),
    WMS("Warehouse Management System");

    private final String description;

    Systems(String description) {
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static Systems fromValue(String value) {
        for (Systems s : values()) {
            if (s.name().equals(value) || s.description.equals(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Неизвестный Systems: " + value);
    }

    @Override
    public String toString() {
        return description;
    }
}

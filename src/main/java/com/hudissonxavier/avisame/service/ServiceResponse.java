package com.hudissonxavier.avisame.service;

import java.util.Map;

public final class ServiceResponse {

    private ServiceResponse() {
    }

    public static Map<String, String> message(String key, String message) {
        return Map.of(key, message);
    }
}


package org.example.bootdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AIService {

    String answer(String prompt) throws Exception;
}

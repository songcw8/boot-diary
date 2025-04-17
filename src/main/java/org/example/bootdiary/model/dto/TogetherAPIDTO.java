package org.example.bootdiary.model.dto;

import org.apache.logging.log4j.message.Message;

import java.util.List;

public record TogetherAPIDTO(String model, List<Message> messages) {
    public static record Message(String role, String content) {}
}

package com.smilep.ptok.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

// TODO - make it an interface
@Service
public class PocketToKindleService {

    private DocumentService documentService;

    private EmailService emailService;

    public PocketToKindleService(DocumentService documentService, EmailService emailService) {
        this.documentService = documentService;
        this.emailService = emailService;
    }

    public void trigger(int count) {
        Path path = documentService.generate(count);
        emailService.send(path);
    }
}

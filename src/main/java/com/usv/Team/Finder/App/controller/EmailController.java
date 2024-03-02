package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.EmailRequestDto;
import com.usv.Team.Finder.App.service.JavaSmtpGmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email") // Defineste baza caii pentru acest controller
public class EmailController {

    private final JavaSmtpGmailSenderService senderService;

    @Autowired
    public EmailController(JavaSmtpGmailSenderService senderService) {
        this.senderService = senderService;
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public String sendEmails(@RequestBody EmailRequestDto emailRequest) {
        senderService.sendEmails(emailRequest.getIdOrganisationAdmin(), emailRequest.getEmailList());
        return "Emails sent successfully!";
    }
}
package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.entity.Invitation;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.InvitationRepository;
import com.usv.Team.Finder.App.repository.OrganisationRepository;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JavaSmtpGmailSenderService {
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;
    private final InvitationRepository invitationRepository;
    @Autowired
    private JavaMailSender emailSender;

    public JavaSmtpGmailSenderService(UserRepository userRepository, OrganisationRepository organisationRepository, InvitationRepository invitationRepository) {
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
        this.invitationRepository = invitationRepository;
    }

    public void sendEmails(UUID idOrganisationAdmin, List<String> emailList) {
        User admin = userRepository.findById(idOrganisationAdmin).orElseThrow(() -> new RuntimeException(ApplicationConstants.ERROR_MESSAGE_USER));
        UUID idOrganisation = admin.getIdOrganisation();
        Organisation organisation = organisationRepository.findById(idOrganisation).orElseThrow(() -> new RuntimeException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION));
        String employeeRegistrationLink = organisation.getRegistrationUrl();

        for (String email : emailList) {
            Invitation invitation = Invitation.builder()
                    .idOrganisationAdmin(admin.getIdUser())
                    .idOrganisation(organisation.getIdOrganisation())
                    .emailEmployee(email)
                    .registered(false)
                    .build();
            invitationRepository.save(invitation);
            sendEmail(email, "Please register using this link: " + employeeRegistrationLink);
        }
    }

    private void sendEmail(String toEmail, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Invitation to Register");
        message.setText(body);
        emailSender.send(message);
        System.out.println("Email sent to " + toEmail);
    }
}
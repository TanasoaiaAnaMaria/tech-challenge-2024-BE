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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
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
            // Încercăm să găsim o invitație existentă pentru e-mailul și organizația dată
            Invitation existingInvitation = invitationRepository.findByEmailEmployeeAndIdOrganisation(email, idOrganisation)
                    .orElse(null);

            if (existingInvitation != null) {
                // The invitation is expired, so we update it and resend the email
                if (existingInvitation.isExpired()) {
                    existingInvitation.setExpired(false);
                    existingInvitation.setSentDate(LocalDateTime.now());
                    invitationRepository.save(existingInvitation);
                    sendEmail(email, buildEmailBody(organisation.getOrganisationName(), employeeRegistrationLink));
                }
                // If the invitation is not expired, do nothing (the invitation is skipped)
            } else {
                // There's no existing invitation, so we create a new one and send the email
                Invitation newInvitation = Invitation.builder()
                        .idOrganisationAdmin(admin.getIdUser())
                        .idOrganisation(organisation.getIdOrganisation())
                        .emailEmployee(email)
                        .registered(false)
                        .expired(false)
                        .sentDate(LocalDateTime.now())
                        .build();
                invitationRepository.save(newInvitation);
                sendEmail(email, buildEmailBody(organisation.getOrganisationName(), employeeRegistrationLink));
            }
        }
    }

    private String buildEmailBody(String organisationName, String registrationLink) {
        return String.format("Hello! You are invited to join %s organization by using Team Finder platform.\n\nAccess this link to join in: %s",
                organisationName, registrationLink);
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

    @Scheduled(cron = "0 0 0 * * ?")
    public void expireInvitations() {
        System.out.println("rulat");
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Invitation> invitations = invitationRepository.findByRegisteredFalseAndSentDateBefore(sevenDaysAgo);
        for (Invitation invitation : invitations) {
            invitation.setExpired(true);
            invitationRepository.save(invitation);
        }
    }
}
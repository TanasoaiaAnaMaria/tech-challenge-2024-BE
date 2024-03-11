package com.usv.Team.Finder.App.service;


import com.usv.Team.Finder.App.dto.*;
import com.usv.Team.Finder.App.entity.Invitation;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.RegistrationException;
import com.usv.Team.Finder.App.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InvitationRepository invitationRepository;
    private final OrganisationService organisationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final OrganisationRepository organisationRepository;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, InvitationRepository invitationRepository, OrganisationService organisationService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService,
                       OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.invitationRepository = invitationRepository;
        this.organisationService = organisationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.organisationRepository = organisationRepository;
    }

    public void registerOrganisationAdmin(RegisterOrganisationAdminDto userDto) {
        Optional<User> existingUser = userRepository.findByeMailAdress(userDto.getEMailAdress());
        if (existingUser.isPresent()) {
            throw new RegistrationException(ApplicationConstants.EMAIL_ALREADY_EXISTS, HttpStatus.UNAUTHORIZED);
        }

        UUID organisationId = organisationService.addOrganisation(new OrganisationDto(userDto.getOrganisationName(), userDto.getHeadquarterAddress(),null));
        String password = passwordEncoder.encode(userDto.getPassword());
        Role role = roleRepository.findByAuthority("ORGANIZATION_ADMIN").orElseGet(() -> roleRepository.save(new Role("ORGANIZATION_ADMIN")));
        Set<Role> authorities = new HashSet<>(Collections.singletonList(role));

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .eMailAdress(userDto.getEMailAdress())
                .password(password)
                .idOrganisation(organisationId)
                .authorities(authorities)
                .isDepartmentManager(false)
                .build();
        userRepository.save(user);
    }
    private Invitation findValidInvitation(String email, UUID idOrganisation) {
        List<Invitation> invitations = invitationRepository.findAllByIdOrganisation(idOrganisation);

        for (Invitation inv : invitations) {
            if (inv.getEmailEmployee().equals(email)) {
                return inv;
            }
        }
        return null;
    }

    public void registerEmployee(RegisterEmployeeDto userDto) {
        organisationRepository.findById(userDto.getIdOrganisation())
                .orElseThrow(() -> new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION));

        Invitation invitation = findValidInvitation(userDto.getEMailAdress(), userDto.getIdOrganisation());
        if (invitation == null || invitation.isExpired()) {
            throw new RegistrationException(invitation != null && invitation.isExpired() ?
                    ApplicationConstants.REGISTRATION_INVITATION_EXPIRED_ERROR :
                    ApplicationConstants.REGISTRATION_EMPLOYEE_ERROR, HttpStatus.UNAUTHORIZED);
        }

        if (!invitation.isRegistered()) {
            invitation.setRegistered(true);
            invitationRepository.save(invitation);
        } else {
            throw new RegistrationException(ApplicationConstants.REGISTRATION_EMPLOYEE_ALREADY_EXIST, HttpStatus.CONFLICT);
        }

        String password = passwordEncoder.encode(userDto.getPassword());
        Role role = roleRepository.findByAuthority("EMPLOYEE").orElseGet(() -> roleRepository.save(new Role("EMPLOYEE")));
        Set<Role> authorities = new HashSet<>(Collections.singletonList(role));

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .eMailAdress(userDto.getEMailAdress())
                .password(password)
                .idOrganisation(userDto.getIdOrganisation())
                .authorities(authorities)
                .isDepartmentManager(false)
                .build();
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginUserDto userDto) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEMailAdress(), userDto.getPassword()));
            String token = tokenService.generateJwt(auth);
            System.out.println(token);
            return new LoginResponseDto(token);
        } catch (AuthenticationException e) {
            return new LoginResponseDto("");
        }
    }

    public void resetPassword(LoginUserDto userDto) {
        User user = userRepository.findByeMailAdress(userDto.getEMailAdress())
                .orElseThrow(() -> new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        String newPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
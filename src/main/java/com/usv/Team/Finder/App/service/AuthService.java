package com.usv.Team.Finder.App.service;


import com.usv.Team.Finder.App.dto.*;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.OrganisationRepository;
import com.usv.Team.Finder.App.repository.RoleRepository;
import com.usv.Team.Finder.App.repository.UserRepository;
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
    public static final String MESAJ_DE_EROARE_ORGANISATION = "Organisation does not exist";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganisationService organisationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final OrganisationRepository organisationRepository;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, OrganisationService organisationService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService,
                       OrganisationRepository organisationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.organisationService = organisationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.organisationRepository = organisationRepository;
    }

    public User registerOrganisationAdmin(RegisterOrganisationAdminDto userDto) {
        UUID organisationId = organisationService.addOrganisation(new OrganisationDto(userDto.getOrganisationName(), userDto.getHeadquarterAddress()));
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
                .build();
        return userRepository.save(user);
    }

    public User registerEmployee(RegisterEmployeeDto userDto) {
        organisationRepository.findById(userDto.getIdOrganisation())
                .orElseThrow(() -> new CrudOperationException(MESAJ_DE_EROARE_ORGANISATION));

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
                .build();
        return userRepository.save(user);
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
}
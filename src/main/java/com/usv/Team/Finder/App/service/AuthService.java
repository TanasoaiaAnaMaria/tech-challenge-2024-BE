package com.usv.Team.Finder.App.service;


import com.usv.Team.Finder.App.dto.LoginResponseDto;
import com.usv.Team.Finder.App.dto.LoginUserDto;
import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.dto.RegisterUserDto;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrganisationService organisationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, OrganisationService organisationService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.organisationService = organisationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User registerUser(RegisterUserDto userDto) {
        UUID organisationId = organisationService.addOrganisation(new OrganisationDto(userDto.getOrganisationName(), userDto.getHeadquarterAddress()));
        String password = passwordEncoder.encode(userDto.getPassword());
        Optional<Role> role = roleRepository.findByAuthority("ORGANISATION_ADMIN");
        Set<Role> authorities = new HashSet<>();
        role.ifPresent(authorities::add);

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
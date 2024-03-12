package com.usv.Team.Finder.App.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.usv.Team.Finder.App.dto.*;
import com.usv.Team.Finder.App.entity.Invitation;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;

import java.util.*;

class AuthServiceTest {

    private AuthService authService;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final InvitationRepository invitationRepository = mock(InvitationRepository.class);
    private final OrganisationService organisationService = mock(OrganisationService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final TokenService tokenService = mock(TokenService.class);
    private final OrganisationRepository organisationRepository = mock(OrganisationRepository.class);

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, roleRepository, invitationRepository, organisationService,
                passwordEncoder, authenticationManager, tokenService, organisationRepository);
    }

    @Test
    void registerOrganisationAdmin_Success() {
        // Setup
        Set<Role> roles = new HashSet<>(Collections.singletonList(new Role("ORGANIZATION_ADMIN")));
        RegisterOrganisationAdminDto dto = new RegisterOrganisationAdminDto("John", "Doe", "john@example.com", "password", roles, "OrgName", "HQ Address");
        when(organisationService.addOrganisation(any(OrganisationDto.class))).thenReturn(UUID.randomUUID());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Action
        User result = authService.registerOrganisationAdmin(dto);

        // Assertion
        assertNotNull(result);
        assertEquals(dto.getFirstName(), result.getFirstName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerEmployee_Success() {
        // Setup
        UUID orgId = UUID.randomUUID();
        Set<Role> roles = new HashSet<>(Collections.singletonList(new Role("EMPLOYEE")));
        RegisterEmployeeDto dto = new RegisterEmployeeDto("Jane", "Doe", "jane.doe@example.com", "password", orgId, roles);

        Invitation validInvitation = new Invitation();
        validInvitation.setEmailEmployee(dto.getEMailAdress()); // Ensure this is non-null
        validInvitation.setIdOrganisation(dto.getIdOrganisation());
        validInvitation.setRegistered(false);

        when(organisationRepository.findById(dto.getIdOrganisation())).thenReturn(Optional.of(new Organisation()));
        when(invitationRepository.findAllByIdOrganisation(dto.getIdOrganisation())).thenReturn(Collections.singletonList(validInvitation));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByAuthority("EMPLOYEE")).thenReturn(Optional.of(new Role("EMPLOYEE")));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Action
        User result = authService.registerEmployee(dto);

        // Assertions
        assertNotNull(result);
        assertEquals(dto.getFirstName(), result.getFirstName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Given
        LoginUserDto loginUserDto = new LoginUserDto("jane@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(tokenService.generateJwt(any(Authentication.class))).thenReturn("dummyJvt");

        // When
        LoginResponseDto response = authService.login(loginUserDto);

        // Then
        assertNotNull(response.getJwt());
        assertEquals("dummyJvt", response.getJwt());
    }

    @Test
    void login_Failure() {
        // Given
        LoginUserDto loginUserDto = new LoginUserDto("jane@example.com", "wrongPassword");
        // Mock the AuthenticationManager to throw BadCredentialsException for these credentials
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // When
        LoginResponseDto response = authService.login(loginUserDto);

        // Then
        assertEquals("", response.getJwt());
    }



}

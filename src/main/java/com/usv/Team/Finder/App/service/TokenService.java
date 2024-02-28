package com.usv.Team.Finder.App.service;


import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.OrganisationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenService {

    public static final String ORGANISATION_ERROR_MESAJ = "Nu exista organizatia";
    private final JwtEncoder jwtEncoder;
    private final OrganisationRepository organisationRepository;

    public TokenService(JwtEncoder jwtEncoder, OrganisationRepository organisationRepository) {
        this.jwtEncoder = jwtEncoder;
        this.organisationRepository = organisationRepository;
    }

    public String generateJwt(Authentication auth) {
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Cast Authentication principal la User pentru a accesa proprietățile specifice
        User user = (User) auth.getPrincipal();

        Organisation organisation = organisationRepository.findById(user.getIdOrganisation()).orElseThrow(() -> new CrudOperationException(ORGANISATION_ERROR_MESAJ));;
        // Construire JwtClaimsSet cu informațiile utilizatorului
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("userId", user.getIdUser())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("eMailAdress", user.getEMailAdress())
                .claim("idOrganisation", user.getIdUser())
                .claim("organisationName", organisation.getOrganisationName())
                .claim("headquarterAddress", organisation.getHeadquarterAddress())
                .claim("roles", scope)
                .build();

        System.out.println(claimsSet);
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String generateEmployeeSignUpURL(UUID organisationId) {
        Instant now = Instant.now();

        Organisation organisation = organisationRepository.findById(organisationId).orElseThrow(() -> new CrudOperationException(ORGANISATION_ERROR_MESAJ));;

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(organisation.getOrganisationName())
                .claim("idOrganisation", organisation.getIdOrganisation())
                .claim("organisationName", organisation.getOrganisationName())
                .claim("headquarterAddress", organisation.getHeadquarterAddress())
                .build();

        System.out.println(claimsSet);
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

}
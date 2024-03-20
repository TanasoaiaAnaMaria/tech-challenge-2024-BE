package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.dto.OrganisationStatisticsDto;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.OrganisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/organisation")
@CrossOrigin("*")
public class OrganisationController {
    private final OrganisationService organisationService;

    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping("/getById")
    public ResponseEntity<OrganisationDto> getOrganisationById(@RequestParam UUID id) {
        try {
            OrganisationDto organisation = organisationService.getOrganisationById(id);
            return ResponseEntity.ok(organisation);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateHeadquarterAddress")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<Organisation> updateOrganisationHeadquarterAddress(
            @RequestParam("idOrganisation") UUID idOrganisation,
            @RequestParam("newHeadquarterAddress") String newHeadquarterAddress) throws Exception {

        organisationService.updateOrganisationHeadquarterAddress(idOrganisation, newHeadquarterAddress);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<OrganisationStatisticsDto> getOrganisationStatistics(@RequestParam UUID idOrganisation) {
        try {
            OrganisationStatisticsDto statistics = organisationService.getOrganisationStatistics(idOrganisation);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

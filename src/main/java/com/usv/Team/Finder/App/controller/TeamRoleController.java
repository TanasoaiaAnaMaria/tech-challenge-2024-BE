package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.TeamRoleDto;
import com.usv.Team.Finder.App.entity.TeamRole;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.TeamRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teamRole")
@CrossOrigin("*")
public class TeamRoleController {
    private final TeamRoleService teamRoleService;

    public TeamRoleController(TeamRoleService teamRoleService) {
        this.teamRoleService = teamRoleService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<List<TeamRole>> getTeameRolesPerOrganisation(@RequestParam UUID idOrganisation) {
        List<TeamRole> teamRoles = teamRoleService.getTeameRolesPerOrganisation(idOrganisation);
        return ResponseEntity.ok(teamRoles);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<TeamRole> getTeamRoleById(@RequestParam UUID id){
        try {
            TeamRole teamRole = teamRoleService.getTeamRoleById(id);
            return ResponseEntity.ok(teamRole);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addTeamRole")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<TeamRole> addTeamRole(@RequestParam UUID idOrganisationAdmin, @RequestBody TeamRoleDto teamRoleDto){
        TeamRole teamRole = teamRoleService.addTeamRole(idOrganisationAdmin, teamRoleDto);
        return new ResponseEntity<>(teamRole, HttpStatus.CREATED);
    }

    @PutMapping("/updateTeamRole")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<TeamRole> updateTeamRole(@RequestParam UUID idTeamRole, @RequestBody TeamRoleDto teamRoleDto){
        teamRoleService.updateTeamRole(idTeamRole, teamRoleDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteTeamRole")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<?> deleteTeamRole(@RequestParam UUID idTeamRole){
        try{
            teamRoleService.deleteTeamRole(idTeamRole);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

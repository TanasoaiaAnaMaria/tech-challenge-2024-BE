package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.DepartmentDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.DepartmentRepository;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final OrganisationService organisationService;
    private final UserRepository userRepository;
    public DepartmentService(DepartmentRepository departmentRepository, OrganisationService organisationService, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.organisationService = organisationService;
        this.userRepository = userRepository;
    }

    public List<Department> getDepartments(UUID idOrganisation){
        Iterable<Department> iterableDepartments = departmentRepository.findByIdOrganisation(idOrganisation);
        List<Department> departments = new ArrayList<>();

        iterableDepartments.forEach(department -> departments.add(Department.builder()
                .idDepartment(department.getIdDepartment())
                .idOrganisation(department.getIdOrganisation())
                .departmentName(department.getDepartmentName())
                .departmentManager(department.getDepartmentManager())
                .users(department.getUsers())
                .skills(department.getSkills())
                .build()));

        return departments;
    }

    public Department getDepartmentById(UUID idDepartment){
        return departmentRepository.findById(idDepartment).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_DEPARTMENT));

    }

    public Department addDepartment(UUID idOrganisationAdmin, DepartmentDto departmentDto){
        organisationService.getOrganisationById(departmentDto.getIdOrganisation());

        User user = userRepository.findById(idOrganisationAdmin).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));
        if(!user.getIdOrganisation().equals(departmentDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);

        Department department = Department.builder()
                .idOrganisation(departmentDto.getIdOrganisation())
                .departmentName(departmentDto.getDepartmentName())
                .departmentManager(departmentDto.getDepartmentManager())
                .build();

        departmentRepository.save(department);
        return department;
    }

    public Department updateDepartment(UUID idDepartment, DepartmentDto departmentDto){
        Department existingDepartment =  departmentRepository.findById(idDepartment).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_DEPARTMENT));

        if(!existingDepartment.getIdOrganisation().equals(departmentDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);

        existingDepartment.setDepartmentName(departmentDto.getDepartmentName());
        existingDepartment.setDepartmentManager(departmentDto.getDepartmentManager());

        departmentRepository.save(existingDepartment);
        return existingDepartment;
    }

    public void deleteDepartment(UUID idDepartment){
        if(departmentRepository.findById(idDepartment).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_DEPARTMENT);

        departmentRepository.deleteById(idDepartment);
    }

    public void updateDepartmentManager(Department department, UUID idDepartmentManager){
        department.setDepartmentManager(idDepartmentManager);
        departmentRepository.save(department);
    }

    public void addSkill(UUID idDepartment, Skill skill){
        Department department = getDepartmentById(idDepartment);
        if (department.getSkills() == null) {
            department.setSkills(new HashSet<>());
        }

        department.getSkills().add(skill);

        departmentRepository.save(department);
    }

    public List<String> findDepartmentNamesBySkillAndOrganisation(UUID idSkill, UUID idOrganisation) {
        List<Department> allDepartments = getDepartments(idOrganisation); // Presupunem că aceasta returnează toate departamentele pentru o organizație
        List<String> departmentNames = new ArrayList<>();

        for (Department department : allDepartments) {
            for (Skill skill : department.getSkills()) {
                if (skill.getIdSkill().equals(idSkill)) {
                    departmentNames.add(department.getDepartmentName());
                    break;
                }
            }
        }

        return departmentNames;
    }
}

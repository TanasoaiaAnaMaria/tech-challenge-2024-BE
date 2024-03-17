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

    private String getDepartmentManagerName(UUID idDepartmentManager) {

            if (idDepartmentManager != null) {
                User manager = userRepository.findById(idDepartmentManager).orElseThrow(() ->
                        new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));
                return manager.getFirstName() + " " + manager.getLastName();
            }

        return null;
    }

    public List<DepartmentDto> getDepartments(UUID idOrganisation){
        Iterable<Department> iterableDepartments = departmentRepository.findByIdOrganisation(idOrganisation);
        List<DepartmentDto> departments = new ArrayList<>();

        iterableDepartments.forEach(department -> departments.add(DepartmentDto.builder()
                .idDepartment(department.getIdDepartment())
                .idOrganisation(department.getIdOrganisation())
                .departmentName(department.getDepartmentName())
                .departmentManager(department.getDepartmentManager())
                .departmentManagerName(getDepartmentManagerName(department.getDepartmentManager()))
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
    public void deleteDepartmentManager(Department department){
        department.setDepartmentManager(null);
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
        List<DepartmentDto> allDepartments = getDepartments(idOrganisation); // Presupunem că aceasta returnează toate departamentele pentru o organizație
        List<String> departmentNames = new ArrayList<>();

        for (DepartmentDto department : allDepartments) {
            for (Skill skill : department.getSkills()) {
                if (skill.getIdSkill().equals(idSkill)) {
                    departmentNames.add(department.getDepartmentName());
                    break;
                }
            }
        }

        return departmentNames;
    }

    public void addSkillToDepartment(Department department, Skill skill) {
        if (department.getSkills() == null) {
            department.setSkills(new HashSet<>());
        }
        department.getSkills().add(skill);
        departmentRepository.save(department);
    }

    public void removeSkillFromDepartment(Department department, Skill skill) {
        if (department.getSkills() != null && department.getSkills().contains(skill)) {
            department.getSkills().remove(skill);
            departmentRepository.save(department);
        }
    }


}

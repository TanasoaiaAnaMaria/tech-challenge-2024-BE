package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.DepartmentDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getDepartments(){
        Iterable<Department> iterableDepartments = departmentRepository.findAll();
        List<Department> departments = new ArrayList<>();

        iterableDepartments.forEach(department -> departments.add(Department.builder()
                .idOrganisation(department.getIdOrganisation())
                .departmentName(department.getDepartmentName())
                .departmentManager(department.getDepartmentManager())
                .build()));

        return departments;
    }

    public Department getDepartmentById(UUID idDepartment){
        return departmentRepository.findById(idDepartment).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_DEPARTMENT));

    }

    public Department addDepartment(DepartmentDto departmentDto){
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

        existingDepartment.setIdOrganisation(departmentDto.getIdOrganisation());
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
}

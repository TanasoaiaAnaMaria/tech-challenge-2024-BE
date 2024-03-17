package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.dto.User_SkillDto;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.entity.User_Skill;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.User_SkillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class User_SkillService {
    public final User_SkillRepository userSkillRepository;
    public final UserService userService;
    public final DepartmentService departmentService;

    public final SkillService skillService;

    public User_SkillService(User_SkillRepository userSkillRepository, UserService userService, DepartmentService departmentService, SkillService skillService) {
        this.userSkillRepository = userSkillRepository;
        this.userService = userService;
        this.departmentService = departmentService;
        this.skillService = skillService;
    }

    public List<User_Skill> getUserSkillsByUserAndApproved(UUID idUser) {
        return userSkillRepository.findByIdUserAndApproved(idUser, true);
    }

    public List<User_Skill> getUserSkillsByUserAndUnapproved(UUID idUser) {
        return userSkillRepository.findByIdUserAndApproved(idUser, false);
    }

    public User_Skill getUserSkillById(UUID idUserSkill){
        return userSkillRepository.findById(idUserSkill).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL));
    }

    public List<User_Skill> getAllUnapprovedUserSkillsByDepartmentManager(UUID idDepartmentManager) {
        User manager = userService.existUser(idDepartmentManager);
        List<User> usersInDepartment = userService.getUsersPerDepartment(manager.getIdDepartment());
        List<User_Skill> unapprovedSkills = new ArrayList<>();
        for (User user : usersInDepartment) {
            unapprovedSkills.addAll(userSkillRepository.findByIdUserAndApproved(user.getIdUser(), false));
        }
        return unapprovedSkills;
    }


    public User_Skill addUserSkill(User_SkillDto userSkillDto){
        User user = userService.existUser(userSkillDto.getIdUser());
        SkillDto skill = skillService.getSkillById(userSkillDto.getIdSkill(), userSkillDto.getIdUser());

        boolean hasDepartment = user.getIdDepartment() != null;
        boolean isDepartmentManager = Boolean.TRUE.equals(user.getIsDepartmentManager());
        boolean approved = !hasDepartment || isDepartmentManager;

        User_Skill userSkill = User_Skill.builder()
                .idUser(userSkillDto.getIdUser())
                .numeUser(user.getFirstName() + " " + user.getLastName())
                .idSkill(userSkillDto.getIdSkill())
                .numeSkill(skill.getSkillName())
                .level(userSkillDto.getLevel())
                .experience(userSkillDto.getExperience())
                .approved(approved)
                .build();

        userSkillRepository.save(userSkill);
        return userSkill;
    }

    public User_Skill updateUserSkill(UUID idUserSkill, User_SkillDto userSkillDto){
        User_Skill existingUserSkill = userSkillRepository.findById(idUserSkill).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL));

        existingUserSkill.setLevel(userSkillDto.getLevel());
        existingUserSkill.setExperience(userSkillDto.getExperience());
        existingUserSkill.setApproved(userSkillDto.isApproved());

        userSkillRepository.save(existingUserSkill);
        return existingUserSkill;
    }

    public User_Skill approveUserSkill(UUID idUserSkill){
        User_Skill existingUserSkill = userSkillRepository.findById(idUserSkill).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL));

        existingUserSkill.setApproved(true);

        userSkillRepository.save(existingUserSkill);
        return existingUserSkill;
    }

    public void desapproveUserSkill(UUID idUserSkill){
        User_Skill existingUserSkill = userSkillRepository.findById(idUserSkill).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL));

        deleteUserSkill(existingUserSkill.getIdUserSkill());
    }

    public void deleteUserSkill(UUID idUserSkill){
        if(userSkillRepository.findById(idUserSkill).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL);

        userSkillRepository.deleteById(idUserSkill);
    }
}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.User_SkillDto;
import com.usv.Team.Finder.App.entity.User_Skill;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.User_SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class User_SkillService {
    public final User_SkillRepository userSkillRepository;

    public User_SkillService(User_SkillRepository userSkillRepository) {
        this.userSkillRepository = userSkillRepository;
    }

    public List<User_Skill> getUserSkillsByUser(UUID idUser) {
        return userSkillRepository.findByIdUser(idUser);
    }

    public User_Skill getUserSkillById(UUID idUserSkill){
        return userSkillRepository.findById(idUserSkill).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL));
    }

    public User_Skill addUserSkill(User_SkillDto userSkillDto){
        User_Skill userSkill = User_Skill.builder()
                .idUser(UUID.fromString(userSkillDto.getIdUser()))
                .idSkill(userSkillDto.getIdSkill())
                .level(userSkillDto.getLevel())
                .experience(userSkillDto.getExperience())
                .approved(userSkillDto.isApproved())
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

    public void deleteUserSkill(UUID idUserSkill){
        if(userSkillRepository.findById(idUserSkill).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_SKILL);

        userSkillRepository.deleteById(idUserSkill);
    }
}

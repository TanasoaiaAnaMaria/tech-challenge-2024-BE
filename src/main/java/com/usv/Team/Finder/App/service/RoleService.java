package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles(){
        Iterable<Role> iterableRole = roleRepository.findAll();
        List<Role> role = new ArrayList<>();

        iterableRole.forEach(role1 -> {
            if (!"EMPLOYEE".equals(role1.getAuthority())) { // Ignore roles with authority "EMPLOYEE"
                role.add(Role.builder()
                        .id(role1.getId())
                        .authority(role1.getAuthority())
                        .build());
            }
        });
        return role;
    }

    public Role getRoleById (Long idRole){
        return roleRepository.findById(idRole).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ROLE));
    }

    public Role addRole(String authority){
        Role role = Role.builder()
                .authority(authority)
                .build();
        roleRepository.save(role);
        return role;
    }

    public void deleteRole(Long idRole){
        if(roleRepository.findById(idRole).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ROLE);

        roleRepository.deleteById(idRole);
    }

    public Role getRoleByAuthority(String authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new RuntimeException("Role not found with authority: " + authority));
    }

}

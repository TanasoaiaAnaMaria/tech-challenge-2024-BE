package com.usv.Team.Finder.App.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.RoleRepository;
import com.usv.Team.Finder.App.entity.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void getRoles_ReturnsRolesExcludingEmployee() {
        Role adminRole = new Role(1L, "ADMIN", new HashSet<>());
        Role employeeRole = new Role(2L, "EMPLOYEE", new HashSet<>());
        when(roleRepository.findAll()).thenReturn(Arrays.asList(adminRole, employeeRole));

        List<Role> roles = roleService.getRoles();

        assertEquals(1, roles.size());
        assertTrue(roles.stream().anyMatch(role -> role.getAuthority().equals("ADMIN")));
        assertFalse(roles.stream().anyMatch(role -> role.getAuthority().equals("EMPLOYEE")));
    }


    @Test
    void getRoleById_ReturnsRole() {
        Role expectedRole = new Role(1L, "ADMIN", null);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(expectedRole));

        Role role = roleService.getRoleById(1L);

        assertEquals(expectedRole, role);
    }

    @Test
    void addRole_CreatesNewRole() {
        when(roleRepository.save(any(Role.class))).thenAnswer(i -> i.getArgument(0));

        Role savedRole = roleService.addRole("NEW_ROLE");

        assertEquals("NEW_ROLE", savedRole.getAuthority());
    }

    @Test
    void deleteRole_DeletesRoleSuccessfully() {
        Role existingRole = new Role(1L, "TO_DELETE", null);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        doNothing().when(roleRepository).deleteById(1L);

        assertDoesNotThrow(() -> roleService.deleteRole(1L));
    }

    @Test
    void getRoleByAuthority_ReturnsRole() {
        Role expectedRole = new Role(1L, "ADMIN", null);
        when(roleRepository.findByAuthority("ADMIN")).thenReturn(Optional.of(expectedRole));

        Role role = roleService.getRoleByAuthority("ADMIN");

        assertEquals(expectedRole, role);
    }

    @Test
    void getRoleById_RoleNotFound() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CrudOperationException.class, () -> roleService.getRoleById(999L));
    }

    @Test
    void deleteRole_RoleNotFound() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CrudOperationException.class, () -> roleService.deleteRole(999L));
    }

    @Test
    void getRoleByAuthority_RoleNotFound() {
        when(roleRepository.findByAuthority("NON_EXISTENT")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.getRoleByAuthority("NON_EXISTENT"));
    }

}

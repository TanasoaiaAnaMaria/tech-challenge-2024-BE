package com.usv.Team.Finder.App.controller;


import com.usv.Team.Finder.App.dto.LoginResponseDto;
import com.usv.Team.Finder.App.dto.LoginUserDto;
import com.usv.Team.Finder.App.dto.RegisterEmployeeDto;
import com.usv.Team.Finder.App.dto.RegisterOrganisationAdminDto;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterOrganisationAdminDto userDto) {
        authService.registerOrganisationAdmin(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<User> registerEmployee(@RequestBody RegisterEmployeeDto userDto) {
        authService.registerEmployee(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto userDto){
        return ResponseEntity.ok().body(authService.login(userDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody LoginUserDto resetPassword) {
        authService.resetPassword(resetPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
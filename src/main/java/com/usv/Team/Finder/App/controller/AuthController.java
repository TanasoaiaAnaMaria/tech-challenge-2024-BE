package com.usv.Team.Finder.App.controller;


import com.usv.Team.Finder.App.dto.LoginResponseDto;
import com.usv.Team.Finder.App.dto.LoginUserDto;
import com.usv.Team.Finder.App.dto.RegisterUserDto;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.service.AuthService;
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
    public ResponseEntity<User> register(@RequestBody RegisterUserDto userDto) {
        return ResponseEntity.ok().body(authService.registerUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto userDto){
        return ResponseEntity.ok().body(authService.login(userDto));
    }
}